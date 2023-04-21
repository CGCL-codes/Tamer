package com.scs.base.service.part;

import java.util.List;
import com.scs.base.model.part.Part;

public interface PartService {

    public List getParts();

    public Part getPart(Long partId);

    public Part savePart(Part part);

    public void removePart(Long partId);

    public Long getPartId(String partName);

    public void saveAllParts(List partList);

    public List getPartsByName(String partName);
}
