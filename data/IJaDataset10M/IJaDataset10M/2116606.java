package desmoj.core.dist;

import desmoj.core.simulator.Model;

/**
 * Uniformly distributed stream of pseudo random numbers of type long. Values
 * produced by this distribution are uniformly distributed in the range
 * specified as parameters of the constructor.
 * 
 * @see desmoj.core.dist.Distribution
 * 
 * @version DESMO-J, Ver. 2.3.4beta copyright (c) 2012
 * @author Tim Lechler
 * @author modified by Philip Joschko
 * @deprecated Replaced by DiscreteDistUniform
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
public class IntDistUniform extends DiscreteDistUniform implements IntDist {

    public IntDistUniform(Model owner, String name, long minValue, long maxValue, boolean showInReport, boolean showInTrace) {
        super(owner, name, minValue, maxValue, showInReport, showInTrace);
    }
}
