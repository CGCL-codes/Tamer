package de.d3s.alricg.logic.regel.interfaces;

import de.d3s.alricg.logic.kommando.Kommando;
import de.d3s.alricg.logic.regel.AbfrageLogList;

/**
 * @author V. Strelow
 *
 */
public interface AlterBestimmen extends Regel {

    public AbfrageLogList<Integer> changeStartAlter(Kommando kommando, AbfrageLogList<Integer> alterInJahren);
}
