package info.metlos.jcdc.fileshare;

import info.metlos.jcdc.config.extensionpoints.VisualFileListProviderFactoryExtensionPoint;
import info.metlos.jcdc.util.INamed;
import info.metlos.jdc.fileshare.CompoundFileListProvider;
import info.metlos.jdc.fileshare.FileListProviderExtensionPoint;
import info.metlos.plugin.IPlugin;

/**
 * Plugins implementing this interface are used as factories for creating file
 * list providers of certain type.
 * <p>
 * jcDC creates one {@link CompoundFileListProvider} and registers it with the
 * jDC's {@link FileListProviderExtensionPoint} as the active file list
 * provider. This compound provider can contain any number of other file list
 * providers that it merges in itself providing one merged file list.
 * <p>
 * Implementations of this interface that register themselves with
 * {@link VisualFileListProviderFactoryExtensionPoint} then function as
 * factories for creating individual file list providers that are to be merged
 * into the compound one. That way we can transparently add new file list
 * providers into the application in a generic manner.
 * 
 * 
 * @author metlos
 * 
 * @version $Id: IVisualFileListProviderFactory.java 99 2007-07-01 01:43:52Z metlos $
 */
public interface IVisualFileListProviderFactory extends INamed, IPlugin {

    /**
	 * @return a new instance of a file list provider type this factory can
	 *         create.
	 */
    IVisualFileListProvider createFileListProvider();

    /**
	 * This method can be used to check whether given provider has the same type
	 * as the providers created using this factory.
	 * 
	 * @param provider
	 *            the provider to check
	 * @return true if the provider has the same type as the providers created
	 *         using this factory, false otherwise.
	 */
    boolean hasSupportedType(IVisualFileListProvider provider);
}
