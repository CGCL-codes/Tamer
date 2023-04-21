package de.mpiwg.vspace.preview.internal;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import de.mpiwg.vspace.common.constants.VSpaceExtensions;
import de.mpiwg.vspace.common.constants.VSpaceFilenames;
import de.mpiwg.vspace.common.project.ProjectObservable;
import de.mpiwg.vspace.common.project.ProjectManager;

public class ResourceManager implements Observer {

    public static final ResourceManager INSTANCE = new ResourceManager();

    private Resource diagramResource;

    private Resource mapDiagramResource;

    private Resource resource;

    private Resource mapResource;

    private Diagram mapDiagram;

    private Diagram diagram;

    public Diagram getMapDiagram() {
        if (mapDiagram == null) {
            init();
        }
        return mapDiagram;
    }

    public void setMapDiagram(Diagram mapDiagram) {
        this.mapDiagram = mapDiagram;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    private ResourceManager() {
        ProjectObservable.INSTANCE.addObserver(this);
        init();
    }

    public void init() {
        diagramResource = null;
        mapDiagramResource = null;
        resource = null;
        mapResource = null;
        TransactionalEditingDomain editingDomain = ProjectObservable.INSTANCE.getEditingDomain();
        if (editingDomain == null) return;
        ResourceSet resourceSet = editingDomain.getResourceSet();
        if (resourceSet == null) return;
        diagramResource = null;
        mapDiagramResource = null;
        if (resourceSet.getResources() != null) {
            for (Resource res : resourceSet.getResources()) {
                if (res.getURI().fileExtension().equals(VSpaceExtensions.DIAGRAM_EXTENSION)) {
                    diagramResource = res;
                    continue;
                }
                if (res.getURI().fileExtension().equals(VSpaceExtensions.OVERVIEW_MAP_DIAGRAM_EXTENSION)) {
                    mapDiagramResource = res;
                    continue;
                }
                if (res.getURI().fileExtension().equals(VSpaceExtensions.MODEL_EXTENSION)) {
                    resource = res;
                    continue;
                }
                if (res.getURI().fileExtension().equals(VSpaceExtensions.OVERVIEW_MAP_EXTENSION)) {
                    mapResource = res;
                    continue;
                }
            }
        }
        if (resource == null) return;
        if (!resource.isLoaded()) try {
            resource.load(GMFResourceFactory.getDefaultLoadOptions());
        } catch (IOException e) {
            return;
        }
        if (diagramResource == null) {
            IProject project = ProjectManager.getInstance().getCurrentProject();
            String workspace = project.getLocation().toOSString() + File.separator;
            File diagramFile = new File(workspace + VSpaceFilenames.DIAGRAM_FILENAME + "." + VSpaceExtensions.DIAGRAM_EXTENSION);
            if (!diagramFile.exists()) return;
            IFile diagramIFile = project.getFile(VSpaceFilenames.DIAGRAM_FILENAME + "." + VSpaceExtensions.DIAGRAM_EXTENSION);
            URI diagramURI = URI.createPlatformResourceURI(diagramIFile.getFullPath().toOSString(), false);
            diagramResource = resource.getResourceSet().getResource(diagramURI, true);
        }
        if (diagramResource == null) return;
        EObject eo = diagramResource.getContents().get(0);
        EObject mapDiagramEObject = null;
        if (mapResource != null) {
            if (!mapResource.isLoaded()) {
                try {
                    mapResource.load(GMFResourceFactory.getDefaultLoadOptions());
                } catch (IOException e) {
                    return;
                }
            }
            if (mapDiagramResource == null) {
                IProject project = ProjectManager.getInstance().getCurrentProject();
                URI mapUri = mapResource.getURI();
                String filename = mapUri.lastSegment();
                int indexExt = filename.indexOf("." + VSpaceExtensions.OVERVIEW_MAP_EXTENSION);
                String id = filename.substring(0, indexExt);
                IFile file = project.getFile(id + "." + VSpaceExtensions.OVERVIEW_MAP_DIAGRAM_EXTENSION);
                mapDiagramResource = mapResource.getResourceSet().getResource(URI.createPlatformResourceURI(file.getFullPath().toOSString(), false), true);
            }
            if (mapDiagramResource != null) {
                if (!mapDiagramResource.isLoaded()) {
                    try {
                        mapDiagramResource.load(GMFResourceFactory.getDefaultLoadOptions());
                    } catch (IOException e) {
                        return;
                    }
                }
                mapDiagramEObject = mapDiagramResource.getContents().get(0);
            }
        }
        if (!(eo instanceof Diagram)) {
            return;
        }
        if (mapDiagramEObject != null && !(mapDiagramEObject instanceof Diagram)) mapDiagramEObject = null;
        diagram = (Diagram) eo;
        if (mapDiagramEObject == null) mapDiagram = null; else mapDiagram = (Diagram) mapDiagramEObject;
    }

    public Resource getDiagramResource() {
        return diagramResource;
    }

    public Resource getMapDiagramResource() {
        return mapDiagramResource;
    }

    public Resource getResource() {
        return resource;
    }

    public Resource getMapResource() {
        return mapResource;
    }

    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof ProjectObservable) {
            if (arg1 instanceof Integer) {
                if ((Integer) arg1 == ProjectObservable.PROJECT_CLOSED) {
                    diagramResource = null;
                    mapDiagramResource = null;
                    resource = null;
                    mapResource = null;
                } else {
                    init();
                }
            }
        }
    }
}
