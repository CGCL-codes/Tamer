package com.tensegrity.palo.xmla.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.parsers.XMLADimensionRequestor;
import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.loader.DimensionLoader;

public class XMLADimensionLoader extends DimensionLoader {

    public static final String DIMENSION_ID_SEP = "|.#.|";

    private Map<String, String[]> dimensionIds = null;

    private Set<String> allIds = null;

    private final XMLAClient xmlaClient;

    public XMLADimensionLoader(DbConnection paloConnection, XMLAClient xmlaClient, DatabaseInfo database) {
        super(paloConnection, database);
        this.xmlaClient = xmlaClient;
    }

    public String[] getAllDimensionIds() {
        if (allIds == null) {
            allIds = new LinkedHashSet<String>();
            if (dimensionIds == null) {
                dimensionIds = new LinkedHashMap<String, String[]>();
            }
            String[] cubeIds = ((XMLAConnection) paloConnection).getCubeLoader(database).getAllCubeIds();
            for (String cubeId : cubeIds) {
                if (!dimensionIds.containsKey(cubeId)) {
                    XMLADimensionRequestor req = new XMLADimensionRequestor((XMLACubeInfo) ((XMLAConnection) paloConnection).getCubeLoader(database).load(cubeId), (XMLAConnection) paloConnection);
                    req.setCatalogNameRestriction(database.getId());
                    req.setCubeNameRestriction(cubeId);
                    XMLADimensionInfo[] dims = req.requestDimensions(xmlaClient);
                    ArrayList<String> dimIds = new ArrayList<String>();
                    for (XMLADimensionInfo dim : dims) {
                        dimIds.add(dim.getId());
                    }
                    dimensionIds.put(cubeId, dimIds.toArray(new String[0]));
                }
                allIds.addAll(Arrays.asList(dimensionIds.get(cubeId)));
            }
        }
        return allIds.toArray(new String[0]);
    }

    public String[] getAllDimensionIdsForCube(CubeInfo cube) {
        if (dimensionIds == null) {
            dimensionIds = new LinkedHashMap<String, String[]>();
        }
        if (!dimensionIds.containsKey(cube.getId())) {
            XMLADimensionRequestor req = new XMLADimensionRequestor((XMLACubeInfo) cube, (XMLAConnection) paloConnection);
            req.setCatalogNameRestriction(database.getId());
            req.setCubeNameRestriction(cube.getId());
            XMLADimensionInfo[] dims = req.requestDimensions(xmlaClient);
            ArrayList<String> dimIds = new ArrayList<String>();
            for (XMLADimensionInfo dim : dims) {
                dimIds.add(dim.getId());
            }
            dimensionIds.put(cube.getId(), dimIds.toArray(new String[0]));
        }
        return dimensionIds.get(cube.getId());
    }

    public DimensionInfo loadByName(String name) {
        DimensionInfo dimInfo = findDimension(name);
        if (dimInfo == null) {
            return loadDimension(name);
        }
        return dimInfo;
    }

    protected final void reload() {
        System.out.println("XMLADimensionLoader::reload.");
    }

    private final DimensionInfo findDimension(String name) {
        Collection<PaloInfo> infos = getLoaded();
        for (PaloInfo info : infos) {
            if (info instanceof DimensionInfo) {
                DimensionInfo dimInfo = (DimensionInfo) info;
                if (dimInfo.getName().equals(name)) return dimInfo;
            }
        }
        return null;
    }

    private final XMLADimensionInfo loadDimension(String name) {
        return null;
    }

    public String[] getDimensionIds(int typeMask) {
        return getAllDimensionIds();
    }
}
