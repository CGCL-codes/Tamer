package com.aplpi.wapreview;

import java.lang.String;
import java.io.IOException;

/**
 * 
 * see http://wapreview.sourceforge.net
 *
 * Copyright (C) 2000 Robert Fuller, Applepie Solutions Ltd. 
 *                    <robert.fuller@applepiesolutions.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *

 * WmlDeckInterface defines the WmlDeck methods.  It exists
 * to prevent a circular dependency between WmlDeck and WmlCard.
 *
 *
 * @author Copyright (c) 2000, Applepie Solutions Ltd.
 * @author Written by Robert Fuller &lt;robert.fuller@applepiesolutions.com&gt;
 *
 * @see WmlDeck
 * @see WmlCardInterface
 */
public interface WmlDeckInterface {

    public String setVar(String varName, String varValue);

    public String getVar(String varName);

    public String onenterforward();

    public String onenterbackward();

    public String ontimer();

    public void ontimer(String s);

    public void onenterforward(String s);

    public void onenterbackward(String s);

    /**
   * Add a card onto the deck.  The card is of type 'Object' in
   * order to avoid circular dependencies between WmlDeckInterface
   * and WmlCardInterface.
   */
    public void addCard(Object o);

    public String resolveUrl(String url);

    public String expandVariables(String s) throws java.io.IOException;
}
