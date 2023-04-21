package org.argouml.uml.ui;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.uml.Profile;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;

public class UMLComboBoxEntry implements Comparable {

    private MModelElement _element;

    private String _shortName;

    /** _longName is composed of an identifier and a name as in Class: String */
    private String _longName;

    private Profile _profile;

    /** _display name will be the same as shortName unless there 
     *  is a name collision */
    private String _displayName;

    /** i am not quite sure what _isPhantom means, it may be that it is an
     *  entry that is not in the model list...pjs */
    private boolean _isPhantom;

    public UMLComboBoxEntry(MModelElement element, Profile profile, boolean isPhantom) {
        _element = element;
        if (element != null) {
            MNamespace ns = element.getNamespace();
            _shortName = profile.formatElement(element, ns);
        } else {
            _shortName = "";
        }
        _profile = profile;
        _longName = null;
        _displayName = _shortName;
        _isPhantom = isPhantom;
    }

    public String toString() {
        return _displayName;
    }

    public void updateName() {
        if (_element != null) {
            MNamespace ns = _element.getNamespace();
            _shortName = _profile.formatElement(_element, ns);
        }
    }

    public void checkCollision(String before, String after) {
        boolean collision = (before != null && before.equals(_shortName)) || (after != null && after.equals(_shortName));
        if (collision) {
            if (_longName == null) {
                _longName = getLongName();
            }
            _displayName = _longName;
        }
    }

    public String getShortName() {
        return _shortName;
    }

    public String getLongName() {
        if (_longName == null) {
            if (_element != null) {
                _longName = _profile.formatElement(_element, null);
            } else {
                _longName = "void";
            }
        }
        return _longName;
    }

    private static MNamespace findNamespace(MNamespace phantomNS, MModel targetModel) {
        MNamespace ns = null;
        MNamespace targetParentNS = null;
        MNamespace parentNS = phantomNS.getNamespace();
        if (parentNS == null) {
            ns = targetModel;
        } else {
            targetParentNS = findNamespace(parentNS, targetModel);
            Collection ownedElements = targetParentNS.getOwnedElements();
            String phantomName = phantomNS.getName();
            String targetName;
            if (ownedElements != null) {
                MModelElement ownedElement;
                Iterator iter = ownedElements.iterator();
                while (iter.hasNext()) {
                    ownedElement = (MModelElement) iter.next();
                    targetName = ownedElement.getName();
                    if (targetName != null && phantomName.equals(targetName)) {
                        if (ownedElement instanceof MPackage) {
                            ns = (MPackage) ownedElement;
                            break;
                        }
                    }
                }
            }
            if (ns == null) {
                ns = targetParentNS.getFactory().createPackage();
                ns.setName(phantomName);
                targetParentNS.addOwnedElement(ns);
            }
        }
        return ns;
    }

    public MModelElement getElement(MModel targetModel) {
        if (_isPhantom && targetModel != null) {
            MNamespace targetNS = findNamespace(_element.getNamespace(), targetModel);
            MModelElement clone = null;
            try {
                clone = (MModelElement) _element.getClass().getConstructor(new Class[] {}).newInstance(new Object[] {});
                clone.setName(_element.getName());
                clone.setStereotype(_element.getStereotype());
                if (clone instanceof MStereotype) {
                    ((MStereotype) clone).setBaseClass(((MStereotype) _element).getBaseClass());
                }
                targetNS.addOwnedElement(clone);
                _element = clone;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            _isPhantom = false;
        }
        return _element;
    }

    public void setElement(MModelElement element, boolean isPhantom) {
        _element = element;
        _isPhantom = isPhantom;
    }

    public int compareTo(final java.lang.Object other) {
        int compare = -1;
        if (other instanceof UMLComboBoxEntry) {
            UMLComboBoxEntry otherEntry = (UMLComboBoxEntry) other;
            compare = 0;
            if (otherEntry != this) {
                if (_element == null) {
                    compare = -1;
                } else {
                    if (otherEntry.getElement(null) == null) {
                        compare = 1;
                    } else {
                        compare = getShortName().compareTo(otherEntry.getShortName());
                        if (compare == 0) {
                            compare = getLongName().compareTo(otherEntry.getLongName());
                        }
                    }
                }
            }
        }
        return compare;
    }

    public void nameChanged(MModelElement element) {
        if (element == _element && _element != null) {
            MNamespace ns = _element.getNamespace();
            _shortName = _profile.formatElement(_element, ns);
            _displayName = _shortName;
            _longName = null;
        }
    }

    public boolean isPhantom() {
        return _isPhantom;
    }
}
