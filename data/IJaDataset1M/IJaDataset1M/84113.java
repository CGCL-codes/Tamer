package org.tex4java.tex.boxworld.font;

import java.awt.*;
import org.tex4java.tex.boxworld.*;

/**
 * The baseclass for all Fonts in TexPresenter.
 * 
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.1.1.1 $
 */
public abstract class PresenterFont {

    String family;

    public abstract String getFamily();

    public abstract double getHeight(char character);

    public abstract double getDepth(char character);

    public abstract double getWidth(char character);

    public abstract Glue getSpace();

    public abstract double getem();

    public abstract double getex();

    public abstract void drawCharacter(String fontDir, Graphics2D g, char character, double x, double y);

    public abstract double getParameter(int index);
}
