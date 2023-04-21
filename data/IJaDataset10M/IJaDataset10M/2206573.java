package org.fao.geonet.services.extent;

import static org.fao.geonet.services.extent.ExtentHelper.DESC;
import static org.fao.geonet.services.extent.ExtentHelper.DESC_COLUMN;
import static org.fao.geonet.services.extent.ExtentHelper.FEATURE;
import static org.fao.geonet.services.extent.ExtentHelper.FEATURE_TYPE;
import static org.fao.geonet.services.extent.ExtentHelper.GEO_ID;
import static org.fao.geonet.services.extent.ExtentHelper.GEO_ID_COLUMN;
import static org.fao.geonet.services.extent.ExtentHelper.ID;
import static org.fao.geonet.services.extent.ExtentHelper.ID_COLUMN;
import static org.fao.geonet.services.extent.ExtentHelper.MODIFIABLE_FEATURE_TYPE;
import static org.fao.geonet.services.extent.ExtentHelper.NUM_RESULTS;
import static org.fao.geonet.services.extent.ExtentHelper.RESPONSE;
import static org.fao.geonet.services.extent.ExtentHelper.SELECTED;
import static org.fao.geonet.services.extent.ExtentHelper.TYPENAME;
import static org.fao.geonet.services.extent.ExtentHelper.WFS;
import static org.fao.geonet.services.extent.ExtentHelper.error;
import static org.fao.geonet.services.extent.ExtentHelper.getSelection;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Xml;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.search.spatial.Pair;
import org.fao.geonet.services.extent.Get.Format;
import org.fao.geonet.services.extent.WFS.FeatureType;
import org.fao.geonet.util.ParamUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureIterator;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * List all the available extents
 * 
 * @author jeichar
 */
public class List implements Service {

    private static final class AllCollection extends AbstractCollection<String> {

        private Collection<?> restricted;

        @Override
        public boolean contains(Object o) {
            if (restricted == null) {
                return true;
            } else {
                return this.restricted.contains(o);
            }
        }

        @Override
        public Iterator<String> iterator() {
            return null;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            restricted = c;
            return true;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    private static final String HREF = "href";

    private Collection<String> defaultTypesToShow;

    private int maxFeatures;

    public void init(String appPath, ServiceConfig params) throws Exception {
        final Iterator types = params.getChildren("typename");
        if (types != null) {
            defaultTypesToShow = new LinkedHashSet<String>();
            while (types.hasNext()) {
                defaultTypesToShow.add(((Element) types.next()).getTextTrim());
            }
        }
        final String max = params.getValue("max", String.valueOf(Integer.MAX_VALUE));
        this.maxFeatures = Integer.parseInt(max);
    }

    /**
     * Returns a string if the parameters are not acceptable. This version
     * returns null (no error)
     */
    protected String validateParams(Element params) {
        return null;
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        final String paramError = validateParams(params);
        if (paramError != null) {
            return error(paramError);
        }
        final GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        final ExtentManager extentMan = gc.getExtentManager();
        final String wfsParam = ParamUtils.findParamText(params, WFS);
        final String typenameParam = ParamUtils.findParamText(params, TYPENAME);
        final Format format = Get.Format.lookup(ParamUtils.findParamText(params, "FORMAT"));
        Collection<String> wfssToShow = extentMan.getWFSs().keySet();
        if (wfsParam != null) {
            wfssToShow = Arrays.asList(wfsParam.split(","));
        }
        Collection<String> typesToShow = new AllCollection();
        if (typenameParam != null) {
            typesToShow = new LinkedHashSet<String>(Arrays.asList(typenameParam.split(",")));
        }
        if (this.defaultTypesToShow != null) {
            typesToShow.retainAll(this.defaultTypesToShow);
        }
        final Element result = new Element(RESPONSE);
        Page page = new Page(params);
        final Map<String, WFS> wfss = extentMan.getWFSs();
        for (final String wfsId : wfssToShow) {
            final WFS wfs = wfss.get(wfsId);
            final Element wfsElem = new Element(WFS);
            wfsElem.setAttribute(ID, wfsId);
            boolean hasMore = false;
            final DataStore ds = wfs.getDataStore();
            for (final FeatureType featureType : wfs.getFeatureTypes()) {
                if (typesToShow.contains(featureType.typename)) {
                    hasMore = listFeatureType(params, context, wfs, wfsElem, ds, featureType, page, format);
                    if (page.limitReached()) {
                        break;
                    }
                }
            }
            Element hasMoreElem = new Element("hasMore");
            result.addContent(hasMoreElem);
            hasMoreElem.setText(String.valueOf(hasMore));
            if (wfsElem.getChildren().size() > 0) {
                result.addContent(wfsElem);
            }
        }
        return result;
    }

    private final boolean listFeatureType(Element params, ServiceContext context, WFS wfs, Element wfsElem, DataStore ds, FeatureType featureType, Page page, Format format) throws Exception {
        final FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = featureType.getFeatureSource();
        final String modifiable = String.valueOf(featureType.isModifiable());
        final String baseURL = context.getBaseUrl();
        final String langCode = context.getLanguage();
        final Element typeElem = new Element(FEATURE_TYPE);
        typeElem.setAttribute(TYPENAME, featureType.typename);
        typeElem.setAttribute(ID_COLUMN, featureType.idColumn);
        typeElem.setAttribute(MODIFIABLE_FEATURE_TYPE, modifiable);
        typeElem.setAttribute(DESC_COLUMN, featureType.descColumn);
        typeElem.setAttribute(GEO_ID_COLUMN, featureType.geoIdColumn);
        java.util.Set<String> properties = new HashSet<String>();
        properties.add(featureType.idColumn);
        if (featureType.descColumn != null) {
            properties.add(featureType.descColumn);
        }
        if (featureType.geoIdColumn != null && !DESC_COLUMN.equals(GEO_ID_COLUMN)) {
            properties.add(featureType.geoIdColumn);
        }
        final Query query = createQuery(params, featureType, properties.toArray(new String[0]), maxFeatures);
        final FeatureIterator<SimpleFeature> features = featureSource.getFeatures(query).features();
        final ExtentSelection selection = getSelection(context);
        try {
            while (features.hasNext() && !page.limitReached()) {
                final SimpleFeature next = features.next();
                if (page.canAddFeature(next)) {
                    createFeatureElem(wfs, featureType, baseURL, langCode, typeElem, selection, next, format);
                }
                page.inc();
            }
            wfsElem.addContent(typeElem);
            return features.hasNext();
        } finally {
            features.close();
        }
    }

    private void createFeatureElem(WFS wfs, FeatureType featureType, final String baseURL, final String langCode, final Element typeElem, final ExtentSelection selection, final SimpleFeature next, Format format) throws IOException, JDOMException {
        final Element featureElem = new Element(FEATURE);
        String id = next.getAttribute(featureType.idColumn).toString();
        try {
            int intId = (int) Double.parseDouble(id);
            id = String.valueOf(intId);
        } catch (NumberFormatException e) {
        }
        featureElem.setAttribute(ID, id);
        final boolean selected = selection != null && selection.ids.contains(Pair.read(featureType, id));
        featureElem.setAttribute(SELECTED, String.valueOf(selected));
        final String href = baseURL + "/srv/" + langCode + "/xml.extent.get?wfs=" + wfs.wfsId + "&format=" + format + "&typename=" + featureType.typename + "&id=" + id;
        featureElem.setAttribute(HREF, href);
        if (featureType.descColumn != null) {
            String descAt = ExtentHelper.decodeDescription((String) next.getAttribute(featureType.descColumn));
            final Element desc = Xml.loadString("<" + DESC + ">" + descAt + "</" + DESC + ">", false);
            featureElem.addContent(desc);
        }
        if (featureType.geoIdColumn != null) {
            String geoIdAt = ExtentHelper.decodeDescription((String) next.getAttribute(featureType.geoIdColumn));
            if (geoIdAt == null) geoIdAt = "";
            final Element geoId = Xml.loadString("<" + GEO_ID + ">" + geoIdAt + "</" + GEO_ID + ">", false);
            featureElem.addContent(geoId);
        }
        typeElem.addContent(featureElem);
    }

    protected Query createQuery(Element params, FeatureType featureType, String[] properties, int maxFeatures) throws Exception {
        final DefaultQuery defaultQuery = new DefaultQuery(featureType.typename, Filter.INCLUDE, properties);
        defaultQuery.setMaxFeatures(maxFeatures);
        return defaultQuery;
    }

    private final class Page {

        final int startOfPage, endOfPage;

        int currentCount = 0;

        public Page(Element params) {
            final String maxParam = ParamUtils.findParamText(params, NUM_RESULTS);
            final int maxFeatures;
            if (maxParam != null) {
                maxFeatures = Integer.parseInt(maxParam);
            } else {
                maxFeatures = List.this.maxFeatures;
            }
            final String pageParam = ParamUtils.findParamText(params, "page");
            int page;
            try {
                page = Integer.parseInt(pageParam);
            } catch (Exception e) {
                page = 1;
            }
            startOfPage = (page - 1) * maxFeatures;
            endOfPage = page * maxFeatures;
        }

        public void inc() {
            currentCount++;
        }

        public boolean canAddFeature(SimpleFeature next) {
            return startOfPage <= currentCount && currentCount < endOfPage;
        }

        public boolean limitReached() {
            return endOfPage <= currentCount;
        }
    }
}
