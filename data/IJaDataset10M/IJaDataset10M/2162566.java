package com.realtime.crossfire.jxclient.faces;

import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for face providers.
 * @author Andreas Kirschbaum
 */
public interface FacesProvider {

    /**
     * Returns the size of faces in pixels.
     * @return the size in pixels
     */
    int getSize();

    /**
     * Returns the face for a face ID. This function returns immediately even if
     * the face is not loaded. A not loaded face will be updated as soon as
     * loading has finished.
     * @param faceNum the face ID
     * @param isUnknownImage returns whether the returned face is the "unknown"
     * face; ignored if <code>null</code>
     * @return the face, or the "unknown" face if the face is not loaded
     */
    @NotNull
    ImageIcon getImageIcon(int faceNum, @Nullable boolean[] isUnknownImage);
}
