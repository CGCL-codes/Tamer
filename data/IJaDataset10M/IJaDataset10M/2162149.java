package org.gbif.checklistbank.service;

import org.gbif.checklistbank.model.Image;
import java.util.Collection;

public interface ImageService extends CRUDService<Image>, UsageRelatedService<Image> {

    public Collection<Image> getPreferredImageByUsage(Collection<Integer> usageIds);

    public Image setPreferredImage(Integer usageId, String url);
}
