package de.xmlsicherheit.utils;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

/**
 * <p>IStorageEditorInput implementation. Interface for a IStorage input to an editor.</p>
 *
 * <p>This plug-in is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.</p>
 *
 * <p>This plug-in is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.</p>
 *
 * <p>You should have received a copy of the GNU Lesser General Public License along
 * with this library;<br>
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA</p>
 *
 * @author Dominik Schadow (info@xml-sicherheit.de), www.xml-sicherheit.de
 * @version 1.6.1, 03.10.2006
 */
public class StringInput extends PlatformObject implements IStorageEditorInput {

    private IStorage storage;

    public StringInput(IStorage storage) {
        this.storage = storage;
    }

    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        return storage.getName();
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public IStorage getStorage() {
        return storage;
    }

    public String getToolTipText() {
        return "String-based file: " + storage.getName();
    }
}
