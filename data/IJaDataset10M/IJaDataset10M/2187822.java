package org.argouml.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
*    This class implements a navigation history
*
*    @author Curt Arnold
*    @since 0.9
*/
public class NavigationHistory {

    private List _history;

    private int _position;

    int _isForwardEnabled = -1;

    int _isBackEnabled = -1;

    /**
     *     Called by a user interface element when a request to 
     *     navigate to a model element has been received.
     */
    public void navigateTo(Object element) {
        if (_history == null) {
            _history = new ArrayList();
            _position = -1;
        }
        _position++;
        int size = _history.size();
        if (_position < size) {
            _history.set(_position, new WeakReference(element));
            for (int i = size - 1; i > _position; i--) {
                _history.remove(i);
            }
        } else {
            _position = size;
            _history.add(new WeakReference(element));
        }
        _isForwardEnabled = -1;
        _isBackEnabled = -1;
    }

    /**
     *    Called by a user interface element when a request to 
     *    open a model element in a new window has been recieved.
     */
    public void open(Object element) {
    }

    /**
     *    Called to navigate to previous selection
     *    returns true if navigation performed
     *
     *    @param attempt false if navigation accomplished by earlier listener
     *    @return true if navigation performed
     */
    public Object navigateBack(boolean attempt) {
        Object target = null;
        if (_history == null || _position <= 0) {
            _isBackEnabled = 0;
        } else {
            WeakReference ref = (WeakReference) _history.get(_position);
            Object current = null;
            if (ref != null) {
                current = ref.get();
            }
            int index;
            for (index = _position - 1; index >= 0 && target == null; index--) {
                ref = (WeakReference) _history.get(index);
                if (ref != null) {
                    target = ref.get();
                    if (target == null || target == current) {
                        _history.set(index, null);
                        target = null;
                    } else {
                        if (target instanceof MFeature) {
                            if (((MFeature) target).getOwner() == null) {
                                target = null;
                            }
                        } else {
                            if (target instanceof MModelElement) {
                                if (((MModelElement) target).getNamespace() == null) {
                                    target = null;
                                }
                            }
                        }
                    }
                }
            }
            if (target == null) {
                _isBackEnabled = 0;
            } else {
                if (attempt) {
                    _position = index + 1;
                    _isForwardEnabled = 1;
                    _isBackEnabled = -1;
                } else {
                    _isBackEnabled = 1;
                }
            }
        }
        return target;
    }

    /**
     *    Called to navigate to next selection
     *    returns true if navigation performed
     *
     *    @param attempt false if navigation accomplished by earlier listener
     *    @return true if navigation performed
     */
    public Object navigateForward(boolean attempt) {
        Object target = null;
        _isForwardEnabled = 0;
        if (_history != null) {
            int size = _history.size();
            if (_position < size - 1) {
                WeakReference ref = (WeakReference) _history.get(_position);
                Object current = null;
                if (ref != null) {
                    current = ref.get();
                }
                int index;
                for (index = _position + 1; index < size && target == null; index++) {
                    ref = (WeakReference) _history.get(index);
                    if (ref != null) {
                        target = ref.get();
                        if (target == null || target == current) {
                            _history.set(index, null);
                            target = null;
                        } else {
                            if (target instanceof MFeature) {
                                if (((MFeature) target).getOwner() == null) {
                                    target = null;
                                }
                            } else {
                                if (target instanceof MModelElement) {
                                    if (((MModelElement) target).getNamespace() == null) {
                                        target = null;
                                    }
                                }
                            }
                        }
                    }
                }
                if (target == null) {
                    _isForwardEnabled = 0;
                } else {
                    if (attempt) {
                        _position = index - 1;
                        _isBackEnabled = 1;
                        _isForwardEnabled = -1;
                    } else {
                        _isForwardEnabled = 1;
                    }
                }
            }
        }
        return target;
    }

    /**  
     *    Returns true if this listener has a target for
     *    a back navigation.  Only one listener needs to
     *    return true for the back button to be enabled.
    */
    public boolean isNavigateBackEnabled() {
        boolean enabled = false;
        if (_isBackEnabled == 1) {
            return true;
        } else {
            if (_isBackEnabled != 0) {
                enabled = navigateBack(false) != null;
            }
        }
        return enabled;
    }

    /**  
     *    Returns true if this listener has a target for
     *    a back navigation.  Only one listener needs to
     *    return true for the back button to be enabled.
    */
    public boolean isNavigateForwardEnabled() {
        boolean enabled = false;
        if (_isForwardEnabled == 1) {
            enabled = true;
        } else {
            if (_isForwardEnabled != 0) {
                enabled = navigateForward(false) != null;
            }
        }
        return enabled;
    }
}
