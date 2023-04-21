package org.fao.waicent.kids.giews.dao;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.fao.waicent.kids.giews.dao.bean.Area;
import org.fao.waicent.kids.giews.dao.bean.Dataset;
import org.fao.waicent.kids.giews.dao.bean.DatasetI;
import org.fao.waicent.kids.giews.dao.bean.DatasetLayer;
import org.fao.waicent.kids.giews.dao.bean.EstimatedRainfallDataset;
import org.fao.waicent.kids.giews.dao.bean.Data_3DimensionsDataset;
import org.fao.waicent.kids.giews.dao.bean.Data_2DimensionsDataset;
import org.fao.waicent.kids.giews.dao.bean.Data_4DimensionsDataset;
import org.fao.waicent.kids.giews.dao.bean.Data_5DimensionsDataset;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.util.CSVTableReader;
import org.fao.waicent.util.DBFTableReader;
import org.fao.waicent.util.Debug;
import org.fao.waicent.util.FileResource;
import org.w3c.dom.Document;
import com.mysql.jdbc.ResultSet;

public class DataHandler {

    private kidsSession kids;

    private DatasetLayer dataset_layer;

    private List layer_list;

    public static String ESTIMATED_RAINFALL = "Estimated Rainfall";

    private DBFTableReader table_reader;

    private String area_code;

    private String area_label;

    private boolean finished;

    private int code_column;

    private int label_column;

    private boolean default_selection;

    private String[] dimensions;

    private DatasetI dataset;

    private List gaul_areas;

    private List areas;

    private boolean available = false;

    DatasetI dataset_i = null;

    public DataHandler() {
    }

    public DatasetI getSearchedData(String dimensions[]) {
        Debug.println("DataHandler getSearchedData START " + new Date(System.currentTimeMillis()));
        if (dimensions == null) {
            return null;
        }
        if (dataset instanceof Data_3DimensionsDataset) {
            HashMap map = new HashMap();
            for (int i = 0; i < dimensions.length; i++) {
                switch(i) {
                    case 0:
                        map.put("dimension_1", dimensions[i]);
                        break;
                    case 1:
                        map.put("dimension_2", dimensions[i]);
                        break;
                    case 2:
                        map.put("dimension_3", dimensions[i]);
                        break;
                }
            }
            map.put("Dataset_ID", dataset.getDatasetID());
            Data_3DimensionsDAO dao = (Data_3DimensionsDAO) DAOFactory.getInstance().getDao(Data_3DimensionsDAO.class);
            ((Data_3DimensionsDataset) dataset).setData_3Dimensions((ArrayList) dao.getData_3DimensionsData(map));
        } else if (dataset instanceof Data_2DimensionsDataset) {
            HashMap map = new HashMap();
            for (int i = 0; i < dimensions.length; i++) {
                switch(i) {
                    case 0:
                        map.put("dimension_1", dimensions[i]);
                        break;
                    case 1:
                        map.put("dimension_2", dimensions[i]);
                        break;
                }
            }
            map.put("Dataset_ID", dataset.getDatasetID());
            Data_2DimensionsDAO dao = (Data_2DimensionsDAO) DAOFactory.getInstance().getDao(Data_2DimensionsDAO.class);
            ((Data_2DimensionsDataset) dataset).setData_2Dimensions((ArrayList) dao.getData_2DimensionsData(map));
        } else if (dataset instanceof Data_4DimensionsDataset) {
            HashMap map = new HashMap();
            for (int i = 0; i < dimensions.length; i++) {
                switch(i) {
                    case 0:
                        map.put("dimension_1", dimensions[i]);
                        break;
                    case 1:
                        map.put("dimension_2", dimensions[i]);
                        break;
                    case 2:
                        map.put("dimension_3", dimensions[i]);
                        break;
                    case 3:
                        map.put("dimension_4", dimensions[i]);
                        break;
                }
            }
            map.put("Dataset_ID", dataset.getDatasetID());
            Data_4DimensionsDAO dao = (Data_4DimensionsDAO) DAOFactory.getInstance().getDao(Data_4DimensionsDAO.class);
            ((Data_4DimensionsDataset) dataset).setData_4Dimensions((ArrayList) dao.getData_4DimensionsData(map));
        } else if (dataset instanceof Data_5DimensionsDataset) {
            HashMap map = new HashMap();
            for (int i = 0; i < dimensions.length; i++) {
                switch(i) {
                    case 0:
                        map.put("dimension_1", dimensions[i]);
                        break;
                    case 1:
                        map.put("dimension_2", dimensions[i]);
                        break;
                    case 2:
                        map.put("dimension_3", dimensions[i]);
                        break;
                    case 3:
                        map.put("dimension_4", dimensions[i]);
                        break;
                    case 4:
                        map.put("dimension_5", dimensions[i]);
                        break;
                }
            }
            map.put("Dataset_ID", dataset.getDatasetID());
            Data_5DimensionsDAO dao = (Data_5DimensionsDAO) DAOFactory.getInstance().getDao(Data_5DimensionsDAO.class);
            ((Data_5DimensionsDataset) dataset).setData_5Dimensions((ArrayList) dao.getData_5DimensionsData(map));
        } else if (dataset instanceof EstimatedRainfallDataset) {
            HashMap map = new HashMap();
            for (int i = 0; i < dimensions.length; i++) {
                switch(i) {
                    case 0:
                        map.put("area_codes", dimensions[i]);
                        break;
                    case 1:
                        map.put("indicator_codes", dimensions[i]);
                        break;
                    case 2:
                        map.put("time_series", dimensions[i]);
                        break;
                }
            }
            map.put("Dataset_ID", dataset.getDatasetID());
            EstimatedRainfallDAO dao = (EstimatedRainfallDAO) DAOFactory.getInstance().getDao(EstimatedRainfallDAO.class);
            ((EstimatedRainfallDataset) dataset).setEstimatedRainfall((ArrayList) dao.getEstimatedRainfallData(map));
        }
        Debug.println("DataHandler getSearchedData END " + new Date(System.currentTimeMillis()));
        return dataset;
    }

    public void loadDatasetLayers(String proj_code, int selected_layer) {
        Debug.println("*** DataHandler loadDatasetLayers START ***");
        int selected_layer_index = -1;
        LayersDAO dao = (LayersDAO) DAOFactory.getInstance().getDao(LayersDAO.class);
        List layers = (ArrayList) dao.getDatasetLayers(proj_code);
        this.layer_list = layers;
        for (int i = 0; i < layers.size(); i++) {
            DatasetLayer d_layer = (DatasetLayer) layers.get(i);
            if (kids.getAttribute("MapTableContext").equals("true")) {
                if (d_layer.getFeature_Name().equals(kids.getMap().getSelectedLayer().getName())) {
                    selected_layer_index = i;
                }
            }
        }
        if (selected_layer_index != -1) {
            DatasetLayer d_layer = (DatasetLayer) layers.get(selected_layer_index);
            d_layer.setDatasets(dao.getDatasets(proj_code, d_layer.getLayer_ID()));
            this.dataset_layer = d_layer;
        } else {
            for (int i = 0; i < layers.size(); i++) {
                DatasetLayer d_layer = (DatasetLayer) layers.get(i);
                if (selected_layer == -1 && i == 0) {
                    selected_layer = d_layer.getLayer_ID();
                }
                if (d_layer.getLayer_ID() == selected_layer) {
                    d_layer.setDatasets(dao.getDatasets(proj_code, d_layer.getLayer_ID()));
                    this.dataset_layer = d_layer;
                    break;
                }
            }
        }
        if (layers.size() > 0) {
            this.available = true;
        } else {
            this.available = false;
        }
        Debug.println("*** DataHandler loadDatasetLayers END ***");
    }

    public DatasetLayer getDatasetLayer() {
        return this.dataset_layer;
    }

    public List getDatasetLayerList() {
        return this.layer_list;
    }

    public DatasetI getDatasetAttributes(Dataset dataset, String layer_path, Integer selected_layer, int code_column, int label_column) {
        Debug.println("getDatasetAttributes START");
        String error_message = "";
        HashMap map = new HashMap();
        try {
            String global_path = this.kids.getMapContext().getGlobalHome();
            layer_path = layer_path.substring(0, layer_path.indexOf("."));
            layer_path = layer_path + ".dbf";
            FileResource fileresource = new FileResource(layer_path, layer_path, global_path);
            Debug.println("fileresource = " + fileresource.getAbsoluteFilename() + "; layer_path = " + layer_path);
            table_reader = new DBFTableReader(fileresource);
            if ((code_column == -1) && (label_column == -1)) {
                System.out.println("Please update  code_column and label_column ");
                if (layer_path != null && layer_path.contains("_1.dbf")) {
                    code_column = table_reader.getColumnIndex("ADM1_CODE");
                    label_column = table_reader.getColumnIndex("ADM1_NAME");
                    finished = false;
                } else if (layer_path != null && layer_path.contains("_2.dbf")) {
                    code_column = table_reader.getColumnIndex("ADM2_CODE");
                    label_column = table_reader.getColumnIndex("ADM2_NAME");
                    finished = false;
                } else if (layer_path != null && layer_path.contains("_3.dbf")) {
                    code_column = table_reader.getColumnIndex("ADM3_CODE");
                    label_column = table_reader.getColumnIndex("ADM3_NAME");
                    finished = false;
                } else if (layer_path != null && (layer_path.contains("cities") || layer_path.contains("markets"))) {
                    code_column = table_reader.getColumnIndex("CODE");
                    label_column = table_reader.getColumnIndex("NAME");
                    finished = false;
                } else if (layer_path != null && layer_path.contains("countries") || layer_path.contains("_0.dbf")) {
                    code_column = table_reader.getColumnIndex("ADM0_CODE");
                    label_column = table_reader.getColumnIndex("ADM0_NAME");
                    finished = false;
                } else {
                    code_column = table_reader.getColumnIndex("CODE");
                    label_column = table_reader.getColumnIndex("NAME");
                    if (code_column == -1 || label_column == -1) {
                        code_column = 0;
                        label_column = 1;
                    }
                }
            }
            areas = new ArrayList();
            HashMap areas_map = new HashMap();
            List area_name = new ArrayList();
            table_reader.rewind();
            while (table_reader.readRow() != -1) {
                area_code = table_reader.getValue(code_column);
                area_label = table_reader.getValue(label_column);
                Area a = new Area();
                a.setArea_Code(area_code);
                a.setArea_Label(area_label);
                if (areas_map.get(area_code) == null) {
                    areas_map.put(area_code, area_label);
                    area_name.add(area_label);
                    areas.add(a);
                }
            }
            Collections.sort(area_name);
            areas = sortArea(areas, area_name);
            gaul_areas = areas;
            map.put("Dataset_ID", new Integer(dataset.getDataset_ID()));
            map.put("Dataset_Label", dataset.getDataset_Label());
            map.put("areas", areas);
            map.put("default", useDefault());
            map.put("Data_Handler", this);
        } catch (Exception e) {
            Debug.println("DataHandler.getDatasetAttributes e:" + e.getMessage());
            e.printStackTrace();
        }
        if (dataset.getDataset_Label().equals(ESTIMATED_RAINFALL)) {
            final EstimatedRainfallDAO er = (EstimatedRainfallDAO) DAOFactory.getInstance().getDao(EstimatedRainfallDAO.class);
            EstimatedRainfallDataset e_dataset = (EstimatedRainfallDataset) er.getEstimatedRainfallDataset(map);
            dataset_i = e_dataset;
        } else {
            final KIDSDatasetDAO kids_dataset = (KIDSDatasetDAO) DAOFactory.getInstance().getDao(KIDSDatasetDAO.class);
            int data_tabletype = kids_dataset.getDatasetDataTableType(dataset.getDataset_ID());
            Debug.println(" data_tabletype: " + data_tabletype);
            if (data_tabletype == 3) {
                Data_3DimensionsDAO dao = (Data_3DimensionsDAO) DAOFactory.getInstance().getDao(Data_3DimensionsDAO.class);
                Data_3DimensionsDataset dd_dataset = (Data_3DimensionsDataset) dao.getData_3DimensionsDataset(map);
                dataset_i = dd_dataset;
            } else if (data_tabletype == 2) {
                Data_2DimensionsDAO dao = (Data_2DimensionsDAO) DAOFactory.getInstance().getDao(Data_2DimensionsDAO.class);
                Data_2DimensionsDataset dd_dataset = (Data_2DimensionsDataset) dao.getData_2DimensionsDataset(map);
                dataset_i = dd_dataset;
            } else if (data_tabletype == 4) {
                Data_4DimensionsDAO dao = (Data_4DimensionsDAO) DAOFactory.getInstance().getDao(Data_4DimensionsDAO.class);
                Data_4DimensionsDataset dd_dataset = (Data_4DimensionsDataset) dao.getData_4DimensionsDataset(map);
                dataset_i = dd_dataset;
            } else if (data_tabletype == 5) {
                Data_5DimensionsDAO dao = (Data_5DimensionsDAO) DAOFactory.getInstance().getDao(Data_5DimensionsDAO.class);
                Data_5DimensionsDataset dd_dataset = (Data_5DimensionsDataset) dao.getData_5DimensionsDataset(map);
                dataset_i = dd_dataset;
            }
        }
        Debug.println("getDatasetAttributes END");
        return dataset_i;
    }

    public void updateDataset(DatasetI dataset_i) {
        Debug.println(" DataHandler updateDataset dataset_i: " + dataset_i);
        if (dataset_i instanceof Data_3DimensionsDataset) {
            final Data_3DimensionsDAO dao = (Data_3DimensionsDAO) DAOFactory.getInstance().getDao(Data_3DimensionsDAO.class);
            dao.updateDataset((Data_3DimensionsDataset) dataset_i);
        } else if (dataset_i instanceof Data_4DimensionsDataset) {
            final Data_4DimensionsDAO dao = (Data_4DimensionsDAO) DAOFactory.getInstance().getDao(Data_4DimensionsDAO.class);
            dao.updateDataset((Data_4DimensionsDataset) dataset_i);
        } else if (dataset_i instanceof Data_2DimensionsDataset) {
            final Data_2DimensionsDAO dao = (Data_2DimensionsDAO) DAOFactory.getInstance().getDao(Data_2DimensionsDAO.class);
            dao.updateDataset((Data_2DimensionsDataset) dataset_i);
        } else if (dataset_i instanceof Data_5DimensionsDataset) {
            final Data_5DimensionsDAO dao = (Data_5DimensionsDAO) DAOFactory.getInstance().getDao(Data_5DimensionsDAO.class);
            dao.updateDataset((Data_5DimensionsDataset) dataset_i);
        } else if (dataset instanceof EstimatedRainfallDataset) {
            final EstimatedRainfallDAO dao = (EstimatedRainfallDAO) DAOFactory.getInstance().getDao(EstimatedRainfallDAO.class);
            dao.updateDataset((EstimatedRainfallDataset) dataset_i);
        }
    }

    private List sortArea(List area_list, List area_names) {
        ArrayList areas = new ArrayList();
        if (area_list.size() == area_names.size()) {
            for (int i = 0; i < area_names.size(); i++) {
                for (int j = 0; j < area_list.size(); j++) {
                    Area a = (Area) area_list.get(j);
                    if (a.getArea_Label().equals(area_names.get(i))) {
                        areas.add(a);
                        break;
                    }
                }
            }
        } else {
            return null;
        }
        return areas;
    }

    public void setDefault(boolean default_selection) {
        this.default_selection = default_selection;
    }

    public boolean useDefault() {
        return this.default_selection;
    }

    public void setDefaultDimensions(String dimensions[]) {
        this.dimensions = dimensions;
    }

    public String[] getDefaultDimensions() {
        return this.dimensions;
    }

    public DatasetI getDataset() {
        return this.dataset;
    }

    public void setDataset(DatasetI dataset) {
        this.dataset = dataset;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setKidsSession(kidsSession kids) {
        this.kids = kids;
    }

    public List getGAUL() {
        return gaul_areas;
    }

    public List getAreas() {
        return this.areas;
    }

    Document document = null;

    public void setDataDocument(Document document) {
        this.document = document;
    }

    public Document getDataDocument() {
        return this.document;
    }

    public String getDatasetTheme() {
        return this.dataset.getDatasetCatalogLabel();
    }

    public String getDataset_Label() {
        return this.dataset.getDatasetCatalogLabel();
    }
}
