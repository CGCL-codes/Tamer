package org.wisigoth.chat.client;

import javax.swing.ImageIcon;

/**
 * An avatar is an image choosed by the user.
 * @author tof
 *
 */
public interface Avatar {

    /**
 * get the image associated to an avatar
 * @return the image associated to an avatar if it exist
 * return null if there is none
 */
    ImageIcon getAvatarImage();
}
