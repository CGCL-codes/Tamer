package org.remus.infomngmnt.core.internal.extension;

import java.io.InputStream;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.remus.infomngmnt.InfomngmntPackage;
import org.remus.infomngmnt.InformationStructureDefinition;
import org.remus.infomngmnt.common.core.streams.StreamCloser;
import org.remus.infomngmnt.core.create.PostCreationHandler;
import org.remus.infomngmnt.core.extension.AbstractInformationRepresentation;
import org.remus.infomngmnt.core.extension.IInfoType;
import org.remus.infomngmnt.core.extension.InformationExtensionManager;
import org.remus.infomngmnt.resources.util.ResourceUtil;
import org.remus.infomngmnt.util.EditingUtil;

/**
 * A object represenation of a registered information type. This class is
 * 
 * @author Tom Seidel <toms@tomosch.de>
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class InfoType implements IInfoType {

    /** The configuration element which comes from the plugin-registry **/
    private final IConfigurationElement configurationElement;

    /** The info-types image **/
    private ImageDescriptor img;

    private Image image;

    private final String type;

    private PostCreationHandler createFactory;

    private final String contributor;

    private final String createFactoryClass;

    private final String imageFilePath;

    private List<String> validTransferTypeIds;

    private final String name;

    private final boolean buildHtml;

    private final boolean excludeFromIndex;

    private final String strucuturePath;

    private InformationStructureDefinition structureDefinition;

    /**
	 * Creates
	 * 
	 * @param configurationElement
	 * @param contributor
	 * @param type
	 * @param createFactoryClass
	 * @param imageFilePath
	 */
    public InfoType(final IConfigurationElement configurationElement, final String contributor, final String name, final String type, final String createFactoryClass, final String imageFilePath, final boolean buildHtml, final boolean excludeFromIndex, final String strucuturePath) {
        this.configurationElement = configurationElement;
        this.contributor = contributor;
        this.name = name;
        this.type = type;
        this.createFactoryClass = createFactoryClass;
        this.imageFilePath = imageFilePath;
        this.buildHtml = buildHtml;
        this.excludeFromIndex = excludeFromIndex;
        this.strucuturePath = strucuturePath;
    }

    /**
	 * Returns the creation factory. Every info-type contributes a separate
	 * implementation of how-to create new information objects
	 * 
	 * @return the factory which creates new info-objects.
	 */
    public PostCreationHandler getPostCreationHandler() {
        if (this.createFactory == null) {
            try {
                if (this.createFactoryClass != null && this.createFactoryClass.trim().length() > 0) {
                    this.createFactory = (PostCreationHandler) this.configurationElement.createExecutableExtension(this.createFactoryClass);
                    this.createFactory.setInfoTypeId(this.type);
                    this.createFactory.setStrucutureDefinition(getStructureDefinition());
                }
            } catch (final CoreException e) {
            }
        }
        return this.createFactory;
    }

    public AbstractInformationRepresentation getInformationRepresentation() {
        try {
            return (AbstractInformationRepresentation) this.configurationElement.createExecutableExtension(InformationExtensionManager.PRESENTATION_ATT);
        } catch (CoreException e) {
            return null;
        }
    }

    public String getType() {
        return this.type;
    }

    public ImageDescriptor getImageDescriptor() {
        if (this.img == null) {
            this.img = AbstractUIPlugin.imageDescriptorFromPlugin(this.contributor, this.imageFilePath);
        }
        return this.img;
    }

    public Image getImage() {
        if (this.image == null) {
            this.image = getImageDescriptor().createImage();
        }
        return this.image;
    }

    public void setValidTransferTypeIds(final List<String> validTransferTypeIds) {
        this.validTransferTypeIds = validTransferTypeIds;
    }

    public List<String> getValidTransferTypeIds() {
        return this.validTransferTypeIds;
    }

    public String getName() {
        return this.name;
    }

    /**
	 * @return the buildHtml
	 */
    public boolean isBuildHtml() {
        return this.buildHtml;
    }

    public boolean isExcludeFromIndex() {
        return this.excludeFromIndex;
    }

    public final InformationStructureDefinition getStructureDefinition() {
        if (this.structureDefinition == null) {
            try {
                InputStream openStream = FileLocator.openStream(Platform.getBundle(this.contributor), new Path(this.strucuturePath), false);
                IFile file = ResourceUtil.createTempFile("xmi");
                file.setContents(openStream, true, false, new NullProgressMonitor());
                StreamCloser.closeStreams(openStream);
                this.structureDefinition = EditingUtil.getInstance().getObjectFromFile(file, InfomngmntPackage.Literals.INFORMATION_STRUCTURE_DEFINITION);
                file.delete(true, new NullProgressMonitor());
            } catch (Exception e) {
                throw new IllegalStateException(NLS.bind("Strucuture definition not accessible for Information-Type \'\'{0}\'\'", this.type));
            }
        }
        return this.structureDefinition;
    }
}
