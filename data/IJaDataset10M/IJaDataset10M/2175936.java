package net.sourceforge.coffea.editors.figures;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import net.sourceforge.coffea.uml2.model.IClassService;
import net.sourceforge.coffea.uml2.model.ITypeService;

/**
 * Figure displaying a class
 * @param <I>
 * Type of service for the displayed class
 */
@SuppressWarnings("restriction")
public class ClassFigure extends InterfaceFigure<IClassService<?, ?>> {

    /**
	 * Class icon creation
	 * @return Created icon
	 */
    public static Image createClassIcon() {
        ImageDescriptor img = JavaPluginImages.DESC_OBJS_CLASS;
        return img.createImage();
    }

    /** Class attributes figure */
    protected PropertiesFigure propertiesFigure;

    /** Class nested classes figure */
    protected ClassesFigure nestedClassesFigure;

    /**
	 * Construction of a figure displaying a class
	 * @param srv
	 * Service for the displayed class
	 */
    public ClassFigure(IClassService<?, ?> srv) {
        super(srv);
        remove(methodFigure);
        propertiesFigure = new PropertiesFigure(typeService.getPropertiesServices());
        List<IClassService<?, ?>> nestedClassesServices = new ArrayList<IClassService<?, ?>>();
        List<ITypeService<?, ?>> tpServices = typeService.getTypesServices();
        for (ITypeService<?, ?> tpService : tpServices) {
            if (tpService instanceof IClassService) {
                nestedClassesServices.add((IClassService<?, ?>) tpService);
            }
        }
        nestedClassesFigure = new ClassesFigure(nestedClassesServices);
        add(propertiesFigure);
        add(methodFigure);
        add(nestedClassesFigure);
    }

    @Override
    protected Image createTypeIcon() {
        return createClassIcon();
    }
}
