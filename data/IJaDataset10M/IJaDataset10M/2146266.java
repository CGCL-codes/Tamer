package org.filecommander.image;

import org.filecommander.services.FileSystemItem;

public interface ImageProvider {

    org.eclipse.swt.graphics.Image getImage(FileSystemItem item);
}
