package desmoj.core.dist;

import desmoj.core.simulator.Model;

/**
 * Empirically distributed stream of pseudo random numbers of type
 * <code>long</code>. Values produced by this distribution follow an empirical
 * distribution which is specified by entries consisting of the observed value
 * and the frequency (probability) this value has been observed to occur. These
 * entries are made by using the <code>addEntry()</code> method. There are a few
 * conditions a user has to meet before actually being allowed to take a sample
 * of this distribution.
 * 
 * @see desmoj.core.dist.Distribution
 * 
 * @version DESMO-J, Ver. 2.3.4beta copyright (c) 2012
 * @author Tim Lechler
 * @deprecated Replaced by DiscreteDistEmpirical
 * 
 *             Licensed under the Apache License, Version 2.0 (the "License");
 *             you may not use this file except in compliance with the License.
 *             You may obtain a copy of the License at
 *             http://www.apache.org/licenses/LICENSE-2.0
 * 
 *             Unless required by applicable law or agreed to in writing,
 *             software distributed under the License is distributed on an
 *             "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *             either express or implied. See the License for the specific
 *             language governing permissions and limitations under the License.
 * 
 */
@Deprecated
public class IntDistEmpirical extends DiscreteDistEmpirical<Long> implements IntDist {

    /**
	 * Constructs an empirical distribution producing integer values. Empirical
	 * distributions have to be initialized manually before use. This is done by
	 * calling the <code>addEntry(long, double)</code> method to add values
	 * defining the behaviour of the desired distribution.s
	 * 
	 * @param owner
	 *            Model : The distribution's owner
	 * @param name
	 *            java.lang.String : The distribution's name
	 * @param showInReport
	 *            boolean : Flag for producing reports
	 * @param showInTrace
	 *            boolean : Flag for producing trace output
	 */
    public IntDistEmpirical(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }

    /**
     * Adds a new entry of an empirical value and its associated frequency.
     * Unlike in deprecated IntDistEmpirical, the frequency that has to be given
     * here is the relative frequency of the value itself, not the value of the
     * distribution function at that value.
     * 
     * @param value
     *            int : The empirical value observed
     * @param frequency
     *            double : The corresponding frequency of the empirical value
     */
    public void addEntry(int value, double frequency) {
        super.addEntry((long) value, frequency);
    }
}
