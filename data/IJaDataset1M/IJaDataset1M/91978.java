package org.swing;

/**
 * <p>Interface that is used for components which support window-specific modality. </p>
 * <p>Project: JModalWindow - window-specific modality. </p>
 * <p>Copyright: Copyright (c) 2001-2006. </p>
 * <p/>
 * <p>This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version. </p>
 * <p/>
 * <p>This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details. </p>
 * <p/>
 * <p>You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA </p>
 *
 * @author Jene Jasper
 * @version 2.0
 */
public interface CloseBlocker {

    /**
     * <p>Set status for modal component to closable or not. </p>
     *
     * @param modalIFrame child internal frame that determines if modal component is closable.
     */
    public void setClosable(JModalInternalFrame modalIFrame);

    /**
     * <p>Get an indication if the modal component is closable or not. </p>
     *
     * @return closable status.
     */
    public boolean isClosable();
}
