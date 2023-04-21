package it.greentone.gui;

import java.awt.Font;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011 GreenTone Developer Team.<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * </code>
 * <br>
 * <br>
 * Provider dei font dell'applicazione
 * 
 * @author Giuseppe Caliendo
 */
public class FontProvider {

    /** Font per titoli */
    public static final Font TITLE_BIG = new Font("SansSerif", Font.BOLD, 30);

    /** Font per titoletti */
    public static final Font TITLE_SMALL = new Font("SansSerif", Font.ITALIC, 20);

    /** Font per paragrafo */
    public static final Font PARAGRAPH_BIG = new Font("SansSerif", Font.BOLD, 15);

    /** Font per visualizzare codice */
    public static final Font CODE = new Font("Monospaced", Font.PLAIN, 12);

    /** Font per la visualizzazione di codice grande */
    public static final Font CODE_BIG = new Font("Monospaced", Font.PLAIN, 20);
}
