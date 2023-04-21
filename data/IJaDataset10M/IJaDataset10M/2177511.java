package diuf.diva.hephaistk.fusion.behaviors;

import java.util.Vector;
import diuf.diva.hephaistk.fusion.FusionNucleus;
import diuf.diva.hephaistk.xml.smuiml.Seq_And;

/**
 * Assignment (only one of)
 * 
 *Copyright (C) 2009 Bruno Dumas (bruno.dumas -at- unifr.ch)
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
public class SeqAndBehavior extends AbstractFusionBehavior {

    public static String identifier = Seq_And.NAME;

    static {
        AbstractFusionBehavior.registerBehavior(new SeqAndBehavior());
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean triggered(Vector<FusionNucleus> fusionNuclei, long currentTimecode, int leadTime) {
        boolean answer = true;
        for (FusionNucleus fusionNucleus : fusionNuclei) {
            if (!answer && fusionNucleus.recentlyTriggered(currentTimecode, leadTime)) {
                for (FusionNucleus fusionNucleus2 : fusionNuclei) {
                    fusionNucleus2.resetTimer();
                }
                return false;
            }
            answer = answer & fusionNucleus.recentlyTriggered(currentTimecode, leadTime);
        }
        return answer;
    }
}
