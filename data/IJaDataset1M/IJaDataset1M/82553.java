package com.vangent.hieos.xutil.services.framework;

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.response.Response;

public interface ContentValidationService {

    public boolean runContentValidationService(Metadata m, Response response) throws MetadataException;
}
