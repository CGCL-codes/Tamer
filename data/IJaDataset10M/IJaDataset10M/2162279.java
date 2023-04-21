package de.hpi.eworld.activitygen.export;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import de.hpi.eworld.model.db.data.CityModel;
import de.hpi.eworld.model.db.data.EdgeModel;
import de.hpi.eworld.model.db.data.ModelElement;
import de.hpi.eworld.model.db.data.RoadSpotModel;
import de.hpi.eworld.model.db.data.RoadSpotModel.Type;
import de.hpi.eworld.model.db.data.SchoolModel;
import de.hpi.eworld.model.db.data.WayModel;

/**
 * Module that extracts the population information from the model and puts it
 * out in a SUMO population file. It is a thread, so run the export with the
 * method <code>start</code>.
 * 
 * @author Simon Siemens
 */
public class SumoPopulationExport extends Thread {

    /**
	 * Converts the time of the given date object in the number of seconds from
	 * the beginning of the day.
	 * 
	 * For the conversion, this method only respects the hour, minute and second
	 * field of the date object.
	 * 
	 * @param time
	 * @return
	 */
    private static final long convertTimeToSeconds(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return ((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE)) * 60 + calendar.get(Calendar.SECOND);
    }

    private final List<RoadSpotModel> busStations = new ArrayList<RoadSpotModel>();

    /**
	 * The single {@code CityModel} instance of the {@code ModelManager}.
	 */
    private CityModel city;

    /**
	 * The XML DOM document during the export. This document is used by several
	 * methods to create new elements. So it is available class-wide.
	 */
    private Document doc;

    /**
	 * List of all Edge elements that should be exported.
	 * 
	 * This list is extracted in {@link #collectModelElements} from the complete
	 * list of all model elements in the model manager.
	 */
    private final List<EdgeModel> edges = new ArrayList<EdgeModel>();

    /**
	 * The file to export into (the target SUMO population file).
	 */
    private final File exportFile;

    /**
	 * List of all SchoolModel elements that should be exported.
	 * 
	 * This list is extracted in {@link #collectModelElements} from the complete
	 * list of all model elements in the model manager.
	 */
    private final List<SchoolModel> schools = new ArrayList<SchoolModel>();

    /**
	 * Configures the export module.
	 * 
	 * @param exportFile
	 *            the file to put the generated XML data in
	 */
    public SumoPopulationExport(File exportFile) {
        this.exportFile = exportFile;
    }

    /**
	 * Generates the bus line element and all its sub-elements and appends them
	 * to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the busLines node should be appended to
	 */
    private void addElementBusLines(Element parentElement) {
        parentElement.appendChild(doc.createElement("busLines"));
    }

    /**
	 * Generates the bus stations element and all its sub-elements and appends
	 * them to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the busStations node should be appended to
	 */
    private void addElementBusStations(Element parentElement) {
        int id = 0;
        Element xBusStations = doc.createElement("busStations");
        parentElement.appendChild(xBusStations);
        for (RoadSpotModel b : busStations) {
            Comment description = doc.createComment(b.getDescription());
            xBusStations.appendChild(description);
            Element xBusStation = doc.createElement("busStation");
            xBusStations.appendChild(xBusStation);
            xBusStation.setAttribute("id", String.valueOf(id));
            ++id;
            xBusStation.setAttribute("edge", b.getWay().getFirstForwardEdge().getInternalID());
            xBusStation.setAttribute("pos", String.valueOf(b.getRelativePositionOnWay()));
        }
    }

    /**
	 * Generates the schools element and all its sub-elements and appends them
	 * to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the schools node should be appended to
	 */
    private void addElementSchools(Element parentElement) {
        Element xSchools = doc.createElement("schools");
        parentElement.appendChild(xSchools);
        long timeInSeconds;
        for (SchoolModel s : schools) {
            Element xSchool = doc.createElement("school");
            xSchool.setAttribute("edge", s.getWay().getFirstForwardEdge().getInternalID());
            xSchool.setAttribute("pos", String.valueOf(s.getRelativePositionOnWay()));
            xSchool.setAttribute("beginAge", String.valueOf(s.getLowerAgeLimit()));
            xSchool.setAttribute("endAge", String.valueOf(s.getUpperAgeLimit()));
            xSchool.setAttribute("capacity", String.valueOf(s.getStudentNumber()));
            timeInSeconds = convertTimeToSeconds(s.getOpeningHour());
            xSchool.setAttribute("opening", String.valueOf(timeInSeconds));
            timeInSeconds = convertTimeToSeconds(s.getClosingHour());
            xSchool.setAttribute("closing", String.valueOf(timeInSeconds));
            xSchools.appendChild(xSchool);
        }
    }

    /**
	 * Generates the city gates element and all its sub-elements and appends
	 * them to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the cityGates node should be appended to
	 */
    private void addElementCityGates(Element parentElement) {
        parentElement.appendChild(doc.createElement("cityGates"));
    }

    /**
	 * Generates the streets element and all its sub-elements and appends them
	 * to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the streets node should be appended to
	 */
    private void addElementStreets(Element parentElement) {
        Element xStreets = doc.createElement("streets");
        parentElement.appendChild(xStreets);
        for (EdgeModel edge : edges) {
            Element xStreet = doc.createElement("street");
            xStreet.setAttribute("edge", edge.getInternalID());
            xStreet.setAttribute("population", String.valueOf(edge.getInhabitantDensity()));
            xStreet.setAttribute("workPosition", String.valueOf(edge.getWorkplaceDensity()));
            xStreets.appendChild(xStreet);
        }
    }

    /**
	 * Generates the work hours element and all its sub-elements and appends
	 * them to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the workHours node should be appended to
	 */
    private void addElementWorkHours(Element parentElement) {
        Element xWorkHours = doc.createElement("workHours");
        parentElement.appendChild(xWorkHours);
        if (city != null) {
            for (Entry<Integer, Double> entry : city.getOpeningHours().entrySet()) {
                Element xOpening = doc.createElement("opening");
                xOpening.setAttribute("hour", String.valueOf(entry.getKey()));
                xOpening.setAttribute("proportion", String.valueOf(entry.getValue()));
                xWorkHours.appendChild(xOpening);
            }
            for (Entry<Integer, Double> entry : city.getClosingHours().entrySet()) {
                Element xClosing = doc.createElement("closing");
                xClosing.setAttribute("hour", String.valueOf(entry.getKey()));
                xClosing.setAttribute("proportion", String.valueOf(entry.getValue()));
                xWorkHours.appendChild(xClosing);
            }
        }
    }

    /**
	 * Generates the population element and all its sub-elements and appends
	 * them to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the population node should be appended to
	 */
    private void addElementPopulation(Element parentElement) {
        Element xPopulation = doc.createElement("population");
        parentElement.appendChild(xPopulation);
        if (city != null) {
            Map<Integer, Double> ageDistribution = city.getAgeDistribution();
            final int inhabitants = city.getInhabitants();
            String[][] attributeValues = new String[ageDistribution.size()][3];
            int i = 0;
            for (Entry<Integer, Double> entry : ageDistribution.entrySet()) {
                attributeValues[i][0] = String.valueOf(entry.getKey());
                if (i > 0) attributeValues[i - 1][1] = String.valueOf(entry.getKey());
                DecimalFormat formater = new DecimalFormat("0");
                attributeValues[i][2] = formater.format(entry.getValue() * inhabitants);
                ++i;
            }
            attributeValues[i - 1][1] = "120";
            for (String[] entry : attributeValues) {
                Element xBracket = doc.createElement("bracket");
                xPopulation.appendChild(xBracket);
                xBracket.setAttribute("beginAge", entry[0]);
                xBracket.setAttribute("endAge", entry[1]);
                xBracket.setAttribute("peopleNbr", entry[2]);
            }
        }
    }

    /**
	 * Generates the parameters element and all its sub-elements and appends
	 * them to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the parameters node should be appended to
	 */
    private void addElementParameters(Element parentElement) {
        Element xParameters = doc.createElement("parameters");
        parentElement.appendChild(xParameters);
        if (city != null) {
            xParameters.setAttribute("carPreference", String.valueOf(city.getCarPreference()));
            xParameters.setAttribute("freeTimeActivityRate", String.valueOf(city.getFreeTimeActivityRate()));
            xParameters.setAttribute("uniformRandomTraffic", String.valueOf(city.getUniformRandomTraffic()));
            xParameters.setAttribute("meanTimePerKmInCity", String.valueOf(1000.0 / city.getMeanSpeed()));
            double departureVariance = city.getDepartureVariation() * city.getDepartureVariation();
            xParameters.setAttribute("carDepartureVariation", String.valueOf(departureVariance));
        }
    }

    /**
	 * Generates the general element and all its sub-elements and appends them
	 * to the provided parent element.
	 * 
	 * @param parentElement
	 *            the element the general node should be appended to
	 */
    private void addElementGeneral(Element parentElement) {
        Element xGeneral = doc.createElement("general");
        parentElement.appendChild(xGeneral);
        if (city != null) {
            xGeneral.setAttribute("inhabitants", String.valueOf(city.getInhabitants()));
            xGeneral.setAttribute("households", String.valueOf(city.getHouseholds()));
            xGeneral.setAttribute("childrenAgeLimit", String.valueOf(city.getChildrenAgeLimit()));
            xGeneral.setAttribute("retirementAgeLimit", String.valueOf(city.getRetirementAgeLimit()));
            xGeneral.setAttribute("carRate", String.valueOf(city.getCarPersonRelation()));
            xGeneral.setAttribute("unemploymentRate", String.valueOf(city.getUnemploymentRate()));
            xGeneral.setAttribute("footDistanceLimit", String.valueOf(city.getWalkingDistanceLimit()));
            xGeneral.setAttribute("incomingTraffic", "0");
            xGeneral.setAttribute("outgoingTraffic", "0");
        }
    }

    /**
	 * Finds the model elements that should be exported and organizes them by
	 * their type in the internal data structures.
	 */
    private void collectModelElements() {
        Collection<ModelElement> modelElements = de.hpi.eworld.core.ModelManager.getInstance().getAllModelElements();
        for (ModelElement me : modelElements) {
            if (me instanceof EdgeModel) {
                edges.add((EdgeModel) me);
            } else if (me instanceof WayModel) {
                WayModel way = (WayModel) me;
                for (EdgeModel edge : way.getForwardEdges()) edges.add(edge);
                for (EdgeModel edge : way.getBackwardEdges()) edges.add(edge);
            } else if (me instanceof SchoolModel) {
                schools.add((SchoolModel) me);
            } else if (me instanceof CityModel) {
                city = (CityModel) me;
            } else if (me instanceof RoadSpotModel) {
                RoadSpotModel r = (RoadSpotModel) me;
                if (r.getEventType() == Type.Busstop) {
                    busStations.add(r);
                }
            }
        }
    }

    /**
	 * Exports the data into the export file.
	 * 
	 * Use the <code>Thread.start</code> method instead of this one.
	 * <code>start</code> implicitly calls this <code>run</code> method then.
	 */
    @Override
    public void run() {
        super.run();
        collectModelElements();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("city");
            doc.appendChild(rootElement);
            addElementGeneral(rootElement);
            addElementParameters(rootElement);
            addElementPopulation(rootElement);
            addElementWorkHours(rootElement);
            addElementStreets(rootElement);
            addElementCityGates(rootElement);
            addElementSchools(rootElement);
            addElementBusStations(rootElement);
            addElementBusLines(rootElement);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(exportFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass());
            logger.error("Error while exporting in a SUMO population file", e);
        }
    }
}
