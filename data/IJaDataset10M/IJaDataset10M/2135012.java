package net.sourceforge.tile3d.service.impl;

import net.sourceforge.tile3d.model.ImageSample;

public interface IImageSampleService {

    public Boolean create(ImageSample p_imageSample);

    public Boolean delete(Long p_ImageSampleId);

    public Boolean update(ImageSample p_imageSample);

    public ImageSample search(Long p_imageSampleId);
}
