package gui;

import tThumb.TnGui;
import tThumb.observers.TnThumbData;
import tThumb.observers.TnThumbObserver;
import core.ImageOperations;
import core.observers.InfoData;
import core.observers.InfoObserver;
import core.observers.OperationData;

/**
 * All gui elements facade.
 * 
 * @author Piotr Zawisza
 * Email: pzawisz@gmail.com
 */
public class GUIFacade implements InfoObserver, TnThumbObserver {

    private ImageInfoGui imageInfoGui;

    private ImageOperations imageOperations;

    private InfoBar infoBar;

    private TnGui tnGui;

    private static GUIFacade instance;

    private GUIFacade() {
    }

    public static GUIFacade getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new GUIFacade();
            return instance;
        }
    }

    /** Update GUI data from InfoSubject (ImageInfo) subject */
    public void update(InfoData data) {
        if (imageInfoGui != null) {
            imageInfoGui.populateFields(data);
        }
        if (infoBar != null) {
            infoBar.populateImageInfoData(data);
        }
    }

    public void update(OperationData opData) {
        if (imageOperations != null) {
        }
    }

    public void tnThumbUpdate(TnThumbData tnThumbData) {
        if (tnGui != null) {
            tnGui.addTile(tnThumbData);
        }
    }

    /**
     * registers any GUI object that should respond on engine calls
     * */
    public void registerSWTElement(Object element) {
        if (element instanceof ImageInfoGui) {
            imageInfoGui = (ImageInfoGui) element;
        } else if (element instanceof InfoBar) {
            infoBar = (InfoBar) element;
        } else if (element instanceof TnGui) {
            tnGui = (TnGui) element;
        } else if (element instanceof Object) {
        }
    }

    /**
     * unregisters any GUI object that should respond on engine calls
     * */
    public void unregisterSWTElement(Object element) {
        if (element instanceof ImageInfoGui) {
            imageInfoGui = null;
        } else if (element instanceof InfoBar) {
            infoBar = null;
        } else if (element instanceof Object) {
        }
    }
}
