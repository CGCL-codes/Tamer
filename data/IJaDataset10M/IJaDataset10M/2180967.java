package desmoj.core.dist;

import desmoj.core.simulator.Model;

/**
 * Erlang distributed stream of pseudo random numbers of type double. Erlang
 * distributed streams are specified by a mean value and their order.
 * 
 * @version DESMO-J, Ver. 2.3.4beta copyright (c) 2012
 * @author Tim Lechler
 * @deprecated Replaced by ContDistErlang
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 */
@Deprecated
public class RealDistErlang extends ContDistErlang implements RealDist {

    public RealDistErlang(Model owner, String name, long order, double mean, boolean showInReport, boolean showInTrace) {
        super(owner, name, order, mean, showInReport, showInTrace);
    }
}
