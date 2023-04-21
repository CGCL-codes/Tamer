package it.hotel.controller.photo;

import it.hotel.controller.hotel.HotelSimpleFormController;
import it.hotel.controller.locale.ILocaleContainer;
import it.hotel.controller.user.IUserContainer;
import it.hotel.model.ImageUtils;
import it.hotel.model.gallery.Gallery;
import it.hotel.model.gallery.manager.IGalleryManager;
import it.hotel.model.photo.Photo;
import it.hotel.model.photo.manager.IPhotoManager;
import it.hotel.model.structure.Structure;
import it.hotel.model.structure.manager.IStructureManager;
import it.hotel.model.user.User;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class PhotoFormController extends HotelSimpleFormController {

    private IUserContainer userContainer = null;

    private IStructureManager structureManager = null;

    private IGalleryManager galleryManager = null;

    private IPhotoManager photoManager = null;

    private ILocaleContainer localeContainer = null;

    /**
	 * @throws
	 * @return
	 */
    @Override
    protected Map referenceData(HttpServletRequest req) throws Exception {
        Map map = new HashMap();
        User user = userContainer.getUser();
        int hotelId = user.getStructureId();
        Structure hotel = (Structure) structureManager.get(hotelId);
        Gallery gallery = (Gallery) galleryManager.get(hotel.getGallery().getId());
        Set<Photo> photos = gallery.getPhotos();
        map.put("hotel", hotel);
        map.put("photos", photos);
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Image image = null;
        Image imageUrl = null;
        int height = -1;
        int width = -1;
        int heightUrl = -1;
        int widthUrl = -1;
        Gallery gallery = null;
        String namePhoto = null;
        String baseUrlPhoto = null;
        Photo photo = (Photo) command;
        if (photo.getImage().length != 0) {
            image = Toolkit.getDefaultToolkit().createImage(photo.getImage());
            int h = image.getHeight(null);
            int w = image.getWidth(null);
            long startTime = System.currentTimeMillis();
            int numberPhoto = Toolkit.getDefaultToolkit().checkImage(image, w, h, null);
            while (h == -1 || w == -1 || (numberPhoto < 8)) {
                h = image.getHeight(null);
                w = image.getWidth(null);
                numberPhoto = Toolkit.getDefaultToolkit().checkImage(image, w, h, null);
                long endTime = System.currentTimeMillis();
                if ((endTime - startTime) > 3000) {
                    h = 100;
                    w = 100;
                    numberPhoto = 100;
                }
            }
            HttpSession session = null;
            session = request.getSession();
            ServletContext context = session.getServletContext();
            String path = context.getRealPath("/");
            namePhoto = java.util.UUID.randomUUID().toString();
            photo.setName(namePhoto);
            User user = userContainer.getUser();
            int hotelId = user.getStructureId();
            Structure hotel = (Structure) structureManager.get(hotelId);
            ImageUtils.upload(namePhoto, path, image, hotelId, hotelId, 400, 400, "big");
            baseUrlPhoto = ImageUtils.upload(namePhoto, path, image, hotelId, hotelId, 50, 50, "small");
            photo.setUrl(baseUrlPhoto);
            photo.setGallery(hotel.getGallery());
            String locale = localeContainer.getLocale();
            String photoLocale = (String) request.getParameter("locale");
            if (!photoLocale.isEmpty()) {
                photo.setLocale(photoLocale);
            } else {
                photo.setLocale(locale);
            }
            localeContainer.setLocale(photo.getLocale());
            photoManager.add(photo);
            localeContainer.setLocale(locale);
            return new ModelAndView("redirect:/photo/new.htm");
        } else return new ModelAndView("redirect:/photo/new.htm");
    }

    @Resource(name = "userContainer")
    public void setUserContainer(IUserContainer userContainer) {
        this.userContainer = userContainer;
    }

    @Resource(name = "galleryManager")
    public void setGalleryManager(IGalleryManager galleryManager) {
        this.galleryManager = galleryManager;
    }

    @Resource(name = "photoManager")
    public void setPhotoManager(IPhotoManager photoManager) {
        this.photoManager = photoManager;
    }

    @Resource(name = "structureManager")
    public void setStructureManager(IStructureManager structureManager) {
        this.structureManager = structureManager;
    }

    @Resource(name = "localeContainer")
    public void setLocaleContainer(ILocaleContainer localeContainer) {
        this.localeContainer = localeContainer;
    }
}
