package net.openalp.languagebuilder.commands;

import net.openalp.generic.swing.CommandListener;
import net.openalp.generic.swing.Console;
import net.openalp.core.LexiconDAO;

/**
 * This file is part of OpenALP.
 * <p/>
 * OpenALP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * OpenALP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with OpenALP.  If not, see <a href='http://www.gnu.org/licenses/'>http://www.gnu.org/licenses/</a>.
 * <p/>
 *
 * Removes a token from the dictionary.
 *
 * @author Adam Scarr
 * @since r37
 */
public class Remove implements CommandListener {

    LexiconDAO lexicon;

    public Remove(LexiconDAO lexicon) {
        this.lexicon = lexicon;
    }

    public String getCommand() {
        return "remove";
    }

    public void runCommand(Console console, String[] argv, int argc) {
        lexicon.remove(argv[1]);
    }
}
