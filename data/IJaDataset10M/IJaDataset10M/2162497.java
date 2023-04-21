package com.busdepot.webapp.domain;

import java.util.List;

public interface CatalogRepository {

    public List<Model> getModelsByName(String modelName);

    public int getModelId(String model, int startYear, int endYear);

    public List<Category> getCategoryByModelId(int id);

    public List<Group> getGroup(int categoryId, int modelId);

    public List<ProductGroup> getProductGroup(int groupId, int categoryId, int modelId);

    public List<BusDepotPart> getPartsByCategoryId(int id);

    public List<BusDepotPart> getPartsByGroupId(int id);

    public List<BusDepotPart> getPartsByProductGroupId(int id);
}
