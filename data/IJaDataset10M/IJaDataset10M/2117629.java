package org.photovault.imginfo.dto;

import org.photovault.imginfo.*;

/**
 Abstract base class for data transfer objects used to move or copy image 
 descriptors between data bases. Subclasses are used and constructed as a part 
 of {@link ImageFileDTO}
 
 @since 0.6.0
 @author Harri Kaimio
 @see ImageFileDTO
 @see ImageDescriptorBase
 @see ImageFileDtoResolver
 */
public abstract class ImageDescriptorDTO {

    /**
     Constructor
     @param img The image used to construct this DTO
     */
    ImageDescriptorDTO(ImageDescriptorBase img) {
        width = img.getWidth();
        height = img.getHeight();
        locator = img.getLocator();
    }

    ImageDescriptorDTO() {
    }

    /**
     Width of the image (in pixels)
     */
    private int width;

    /**
     Height of the image (in pixels)
     */
    private int height;

    /**
     Location of the image in containing file
     */
    private String locator;

    /**
     Create a new image descriptor based on this DTO. Used internally by 
     {@link ImageFileDtoResolver}. The method first calls createImageDescriptor()
     to instantiate correct subclass of ImageDescriptorBAse. After that it calls
     updateDescriptor() to set the fields to correct values.
     @param resolver The resolver used to find or create referenced ImageFile 
     instances
     @return A new image descriptor that corresponds to this DTO. The returned 
     instance is not persistent yet.
     */
    ImageDescriptorBase getImageDescriptor(ImageFileDtoResolver resolver) {
        ImageDescriptorBase img = createImageDescriptor();
        updateDescriptor(img, resolver);
        return img;
    }

    /**
     Creates a correct subclass of {@link ImageDescriptorBase} to be used with 
     this DTO class. Derived classes must override this to return new object of 
     the right class.     
     @return Newly created instance of corresponding class.
     */
    protected abstract ImageDescriptorBase createImageDescriptor();

    /**
     Set the fields of given image descriptor to match this DTO. Derived classes
     must override this method if they define new fields.
     @param img The iamge descriptor being modified.
     @param fileResolver Resolver used to find possible {@link ImageFile} 
     references.
     */
    protected void updateDescriptor(ImageDescriptorBase img, ImageFileDtoResolver fileResolver) {
        img.setHeight(getHeight());
        img.setWidth(getWidth());
        img.setLocator(getLocator());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLocator() {
        return locator;
    }
}
