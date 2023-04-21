package com.scythebill.birdlist.model.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.Partial;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.scythebill.birdlist.model.io.PartialIO;
import com.scythebill.birdlist.model.sighting.Location;
import com.scythebill.birdlist.model.sighting.LocationSet;
import com.scythebill.birdlist.model.sighting.ReportSet;
import com.scythebill.birdlist.model.sighting.Sighting;
import com.scythebill.birdlist.model.sighting.Location.Type;
import com.scythebill.birdlist.model.sighting.LocationSet.Builder;
import com.scythebill.birdlist.model.sighting.SightingInfo.PopulationStatus;
import com.scythebill.birdlist.model.taxa.Taxon;
import com.scythebill.birdlist.model.taxa.Taxonomy;
import com.scythebill.birdlist.model.taxa.TaxonomyMapping.UpgradeTracker;
import com.scythebill.birdlist.model.taxa.TaxonomyMappings;
import com.scythebill.xml.BaseNodeParser;
import com.scythebill.xml.NodeParser;
import com.scythebill.xml.ParseContext;
import com.scythebill.xml.TreeBuilder;

/**
 * Reads an XML-formatted report set.
 */
public class XmlReportSetImport {

    public static final String REPORT_SET_SUFFIX = ".bsxm";

    /**
   * @param in a reader on the XML file
   * @param taxonomy the Taxonomy for the file
   * @return the ReportSet
   * @throws IOException
   */
    public ReportSet importReportSet(Reader in, Taxonomy taxonomy, TaxonomyMappings mappings) throws IOException, SAXException {
        TreeBuilder<ReportSet> builder = new TreeBuilder<ReportSet>(null, ReportSet.class);
        return builder.parse(new InputSource(in), new ReportSetParser(taxonomy, mappings));
    }

    private static class ReportSetParser extends BaseNodeParser {

        private final Taxonomy taxonomy;

        private LocationSet locations;

        private List<Sighting> sightings;

        private final TaxonomyMappings mappings;

        private UpgradeTracker taxonomyTracker;

        public ReportSetParser(Taxonomy taxonomy, TaxonomyMappings mappings) {
            this.taxonomy = taxonomy;
            this.mappings = mappings;
        }

        @Override
        public void addCompletedChild(ParseContext context, String namespaceURI, String localName, Object child) throws SAXParseException {
            if (child instanceof LocationSet) {
                locations = (LocationSet) child;
            } else if (child instanceof Sighting) {
                sightings.add((Sighting) child);
            }
        }

        @Override
        public Object endElement(ParseContext context, String namespaceURI, String localName) throws SAXParseException {
            ReportSet reportSet = new ReportSet(locations, sightings);
            reportSet.setTaxonomyUpgrade(taxonomyTracker);
            return reportSet;
        }

        @Override
        public NodeParser startChildElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
            if (XmlReportSetExport.ELEMENT_LOCATIONS.equals(localName)) {
                if (locations != null) throw new SAXParseException("Duplicate <locations> elements", context.getLocator());
                return new LocationSetParser();
            } else if (XmlReportSetExport.ELEMENT_SIGHTING.equals(localName)) {
                if (locations == null) throw new SAXParseException("No <locations> element, or <sighting> element before <locations>", context.getLocator());
                return new SightingsParser();
            }
            return null;
        }

        @Override
        public void startElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
            sightings = new ArrayList<Sighting>(5000);
            String taxonomyId = attrs.getValue("taxonomy");
            if (taxonomyId == null) {
                taxonomyId = "clements6_4";
            }
            if (taxonomy.getId().equals(taxonomyId)) {
                taxonomyTracker = null;
            } else {
                try {
                    taxonomyTracker = mappings.getTracker(taxonomyId);
                } catch (IOException e) {
                    throw new SAXParseException("Failed to load taxonomy updater", context.getLocator(), e);
                }
            }
        }

        public class SightingsParser extends BaseNodeParser {

            private Sighting sighting;

            private StringBuilder description;

            @Override
            public void addText(ParseContext context, char[] text, int start, int length) throws SAXParseException {
                if (description == null) {
                    description = new StringBuilder();
                }
                description.append(text, start, length);
            }

            @Override
            public void addWhitespace(ParseContext context, char[] text, int start, int length) throws SAXParseException {
                if (description != null) {
                    description.append(text, start, length);
                }
            }

            @Override
            public Object endElement(ParseContext context, String namespaceURI, String localName) throws SAXParseException {
                if (description != null) {
                    sighting.getSightingInfo().setDescription(description.toString());
                }
                return sighting;
            }

            @Override
            public void startElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
                String taxonId = getRequiredAttribute(context, attrs, XmlReportSetExport.ATTRIBUTE_TAXON);
                String locationId = getRequiredAttribute(context, attrs, XmlReportSetExport.ATTRIBUTE_LOCATION);
                if (taxonomyTracker != null) {
                    taxonId = taxonomyTracker.getTaxonId(taxonId);
                }
                Taxon taxon = taxonomy.getTaxon(taxonId);
                if (taxon == null) {
                    throw new SAXParseException("Could not find taxon id " + taxonId, context.getLocator());
                }
                Location location = locations.getLocation(locationId);
                if (location == null) {
                    throw new SAXParseException("Could not find location id " + locationId, context.getLocator());
                }
                Sighting sighting = new Sighting(location, taxon);
                String dateStr = attrs.getValue(XmlReportSetExport.ATTRIBUTE_DATE);
                if (dateStr != null) {
                    try {
                        Partial datePartial = PartialIO.fromString(dateStr);
                        sighting.setDateAsPartial(datePartial);
                    } catch (IllegalArgumentException iae) {
                        logError(context, "Could not parse date " + dateStr, iae);
                    }
                }
                String countStr = attrs.getValue(XmlReportSetExport.ATTRIBUTE_COUNT);
                if (countStr != null) {
                    sighting.getSightingInfo().setNumber(Integer.parseInt(countStr));
                }
                if ("true".equals(attrs.getValue(XmlReportSetExport.ATTRIBUTE_MALE))) {
                    sighting.getSightingInfo().setMale(true);
                }
                if ("true".equals(attrs.getValue(XmlReportSetExport.ATTRIBUTE_FEMALE))) {
                    sighting.getSightingInfo().setFemale(true);
                }
                if ("true".equals(attrs.getValue(XmlReportSetExport.ATTRIBUTE_IMMATURE))) {
                    sighting.getSightingInfo().setImmature(true);
                }
                if ("true".equals(attrs.getValue(XmlReportSetExport.ATTRIBUTE_HEARD_ONLY))) {
                    sighting.getSightingInfo().setHeardOnly(true);
                }
                String popStatusId = attrs.getValue(XmlReportSetExport.ATTRIBUTE_POPULATION_STATUS);
                if (popStatusId != null) {
                    PopulationStatus populationStatus = PopulationStatus.forId(popStatusId);
                    if (populationStatus == null) {
                        logWarning(context, "Invalid population status: " + popStatusId);
                    } else {
                        sighting.getSightingInfo().setPopulationStatus(populationStatus);
                    }
                }
                this.sighting = sighting;
            }
        }
    }

    /**
   * Parser for Locations objects.
   */
    public static class LocationSetParser extends BaseNodeParser {

        private LocationSet.Builder locationsBuilder;

        @Override
        public Object endElement(ParseContext context, String namespaceURI, String localName) throws SAXParseException {
            return locationsBuilder.build();
        }

        @Override
        public void startElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
            locationsBuilder = new LocationSet.Builder();
        }

        @Override
        public NodeParser startChildElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
            return getLocationParser(localName, null, locationsBuilder);
        }
    }

    /**
   * Parser for Location elements.
   */
    public static class LocationParser extends BaseNodeParser {

        private Location location;

        private final Type type;

        private final Location parent;

        private final LocationSet.Builder locations;

        public LocationParser(Type type, Location parent, LocationSet.Builder locationsBuilder) {
            this.type = type;
            this.parent = parent;
            locations = locationsBuilder;
        }

        @Override
        public NodeParser startChildElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
            return getLocationParser(localName, location, locations);
        }

        @Override
        public void startElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
            String id = getRequiredAttribute(context, attrs, XmlReportSetExport.ATTRIBUTE_ID);
            String name = getRequiredAttribute(context, attrs, XmlReportSetExport.ATTRIBUTE_NAME);
            Location.Builder locationBuilder = Location.builder().setName(name).setType(type).setParent(parent);
            String ebirdCode = attrs.getValue(XmlReportSetExport.ATTRIBUTE_EBIRD_CODE);
            if (ebirdCode != null) {
                locationBuilder.setEbirdCode(ebirdCode);
            }
            location = locationBuilder.build();
            locations.addLocation(location, id);
        }

        @Override
        public Object endElement(ParseContext context, String namespaceURI, String localName) throws SAXParseException {
            return location;
        }
    }

    /**
   * Create the right NodeParser for a location.
   */
    private static NodeParser getLocationParser(String localName, Location parent, Builder builder) {
        Location.Type type;
        if (localName.equals(XmlReportSetExport.ELEMENT_COUNTRY)) {
            type = Location.Type.country;
        } else if (localName.equals(XmlReportSetExport.ELEMENT_CITY)) {
            type = Location.Type.city;
        } else if (localName.equals(XmlReportSetExport.ELEMENT_COUNTY)) {
            type = Location.Type.county;
        } else if (localName.equals(XmlReportSetExport.ELEMENT_STATE)) {
            type = Location.Type.state;
        } else if (localName.equals(XmlReportSetExport.ELEMENT_REGION)) {
            type = Location.Type.region;
        } else if (localName.equals(XmlReportSetExport.ELEMENT_LOCATION)) {
            type = null;
        } else {
            return null;
        }
        return new LocationParser(type, parent, builder);
    }
}
