package desmoj.core.report;

/**
 * Reports all information about a DiscreteDistGeo distribution.
 * 
 * @version DESMO-J, Ver. 2.3.4beta copyright (c) 2012
 * @author Peter Wueppen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
public class DiscreteDistGeoReporter extends DistributionReporter {

    /**
	 * Creates a new DiscreteDistGeoReporter.
	 * 
	 * @param informationSource
	 *            desmoj.core.simulator.Reportable : The DiscreteDistGeo distribution to report
	 *            about
	 */
    public DiscreteDistGeoReporter(desmoj.core.simulator.Reportable informationSource) {
        super(informationSource);
        groupID = 176;
    }

    /**
	 * Returns the array of strings containing all information about the
	 * DiscreteDistGeo distribution.
	 * 
	 * @return java.lang.String[] : The array of Strings containing all
	 *         information about the DiscreteDistGeo distribution
	 */
    public java.lang.String[] getEntries() {
        if (source instanceof desmoj.core.dist.DiscreteDistGeo) {
            desmoj.core.dist.DiscreteDistGeo td = (desmoj.core.dist.DiscreteDistGeo) source;
            entries[0] = td.getName();
            entries[1] = td.resetAt().toString();
            entries[2] = Long.toString(td.getObservations());
            entries[3] = "Discrete Geometrical";
            entries[4] = Double.toString(td.getProbability());
            entries[5] = "";
            entries[6] = " ";
            entries[7] = Long.toString(td.getInitialSeed());
        } else {
            for (int i = 0; i < numColumns; i++) {
                entries[i] = "Invalid source!";
            }
        }
        return entries;
    }
}
