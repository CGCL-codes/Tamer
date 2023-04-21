package org.mathIT.quantum.stabilizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.mathIT.quantum.Register;
import static java.lang.Math.*;
import static org.mathIT.quantum.stabilizer.LocalCliffordOperator.*;

/**
 *  This class represents the states of a quantum register consisting of 
 *  stabilizer states.
 *  Internally, the quantum computations are performed by the graph state
 *  formalism in which each qubit is represented by a vertex and entanglement
 *  of two qubits by an edge connecting their corresponding vertices.
 *  <p>
 *  For a detailed description of the graphic state formalism and its
 *  algorithms see the article
 *  </p>
 *  <p>
 *  S. Anders, H. J. Briegel: 
 *  'Fast simulation of Stabilizer Circuits using a Graph States Formalism',
 *  <i>Phys. Rev. A</i> <b>73</b>, 022334 (2006)
 *  DOI: 
 *  <a href="http://dx.doi.org/10%2E1103/PhysRevA%2E73%2E022334" target="_top">
 *  10.1103/PhysRevA.73.022334</a>
 *  (Preprint:
 *  <a href="http://arxiv.org/abs/quant-ph/0504117" target="_top">quant-ph/0504117</a>)
 *  </p>
 *  This class is based essentially on the C++ program graphsim.cpp, version 0.10 
 *  from 2005/01/27 written by Simon Anders, downloadable under
 *  <a href="http://homepage.uibk.ac.at/~c705213/work/graphsim.html">
 *           http://homepage.uibk.ac.at/~c705213/work/graphsim.html</a>
 *  @author  Andreas de Vries
 *  @version 1.0
 */
public class GraphRegister {

    /** A lookup table on how any LC operator can be composed from them
    *  generators spiZ (denoted 'V') and smiX (denoted 'U').*/
    private static String[] comp_tbl = { "UUUU", "UU", "VVUU", "VV", "VUU", "V", "VVV", "UUV", "UVU", "UVUUU", "UVVVU", "UUUVU", "UVV", "VVU", "UUU", "U", "VVVU", "UUVU", "VU", "VUUU", "UUUV", "UVVV", "UV", "UVUU" };

    /** 
    * Auxiliary lookup table to compute the c-phase gate.
    * Structure of the table:
    * first index: whether there was an edge between the operands before
    *   (0 = no, 1 = yes)
    * second and third index: byprod op of v1 and v2
    * third index: information to obtain:
    *    0 = whether after the cphase there is an edges
    *    1,2 = new values of the byprod ops of v1 and v2Id
    */
    private static int[][][][] cphase_tbl = { { { { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 5 }, { 1, 0, 5 }, { 1, 0, 6 }, { 1, 0, 6 }, { 0, 3, 8 }, { 0, 3, 8 }, { 0, 0, 10 }, { 0, 0, 10 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 6 }, { 1, 0, 6 }, { 1, 0, 5 }, { 1, 0, 5 }, { 0, 0, 10 }, { 0, 0, 10 }, { 0, 3, 8 }, { 0, 3, 8 } }, { { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 5 }, { 1, 0, 5 }, { 1, 0, 6 }, { 1, 0, 6 }, { 0, 2, 8 }, { 0, 2, 8 }, { 0, 0, 10 }, { 0, 0, 10 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 6 }, { 1, 0, 6 }, { 1, 0, 5 }, { 1, 0, 5 }, { 0, 0, 10 }, { 0, 0, 10 }, { 0, 2, 8 }, { 0, 2, 8 } }, { { 1, 2, 3 }, { 1, 0, 1 }, { 1, 0, 2 }, { 1, 2, 0 }, { 1, 0, 4 }, { 1, 2, 6 }, { 1, 2, 5 }, { 1, 0, 7 }, { 0, 0, 8 }, { 0, 0, 8 }, { 0, 2, 10 }, { 0, 2, 10 }, { 1, 0, 2 }, { 1, 0, 2 }, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 7 }, { 1, 0, 7 }, { 1, 0, 4 }, { 1, 0, 4 }, { 0, 2, 10 }, { 0, 2, 10 }, { 0, 0, 8 }, { 0, 0, 8 } }, { { 1, 3, 0 }, { 1, 0, 1 }, { 1, 0, 2 }, { 1, 3, 3 }, { 1, 0, 4 }, { 1, 3, 5 }, { 1, 3, 6 }, { 1, 0, 7 }, { 0, 0, 8 }, { 0, 0, 8 }, { 0, 3, 10 }, { 0, 3, 10 }, { 1, 0, 2 }, { 1, 0, 2 }, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 7 }, { 1, 0, 7 }, { 1, 0, 4 }, { 1, 0, 4 }, { 0, 3, 10 }, { 0, 3, 10 }, { 0, 0, 8 }, { 0, 0, 8 } }, { { 1, 4, 3 }, { 1, 4, 3 }, { 1, 4, 0 }, { 1, 4, 0 }, { 1, 4, 6 }, { 1, 4, 6 }, { 1, 4, 5 }, { 1, 4, 5 }, { 0, 6, 8 }, { 0, 6, 8 }, { 0, 4, 10 }, { 0, 4, 10 }, { 1, 4, 0 }, { 1, 4, 0 }, { 1, 4, 3 }, { 1, 4, 3 }, { 1, 4, 5 }, { 1, 4, 5 }, { 1, 4, 6 }, { 1, 4, 6 }, { 0, 4, 10 }, { 0, 4, 10 }, { 0, 6, 8 }, { 0, 6, 8 } }, { { 1, 5, 0 }, { 1, 5, 0 }, { 1, 5, 3 }, { 1, 5, 3 }, { 1, 5, 5 }, { 1, 5, 5 }, { 1, 5, 6 }, { 1, 5, 6 }, { 0, 6, 8 }, { 0, 6, 8 }, { 0, 5, 10 }, { 0, 5, 10 }, { 1, 5, 3 }, { 1, 5, 3 }, { 1, 5, 0 }, { 1, 5, 0 }, { 1, 5, 6 }, { 1, 5, 6 }, { 1, 5, 5 }, { 1, 5, 5 }, { 0, 5, 10 }, { 0, 5, 10 }, { 0, 6, 8 }, { 0, 6, 8 } }, { { 1, 6, 0 }, { 1, 5, 1 }, { 1, 5, 2 }, { 1, 6, 3 }, { 1, 5, 4 }, { 1, 6, 5 }, { 1, 6, 6 }, { 1, 5, 7 }, { 0, 5, 8 }, { 0, 5, 8 }, { 0, 6, 10 }, { 0, 6, 10 }, { 1, 5, 2 }, { 1, 5, 2 }, { 1, 5, 1 }, { 1, 5, 1 }, { 1, 5, 7 }, { 1, 5, 7 }, { 1, 5, 4 }, { 1, 5, 4 }, { 0, 6, 10 }, { 0, 6, 10 }, { 0, 5, 8 }, { 0, 5, 8 } }, { { 1, 6, 0 }, { 1, 4, 2 }, { 1, 4, 1 }, { 1, 6, 3 }, { 1, 4, 7 }, { 1, 6, 5 }, { 1, 6, 6 }, { 1, 4, 4 }, { 0, 4, 8 }, { 0, 4, 8 }, { 0, 6, 10 }, { 0, 6, 10 }, { 1, 4, 1 }, { 1, 4, 1 }, { 1, 4, 2 }, { 1, 4, 2 }, { 1, 4, 4 }, { 1, 4, 4 }, { 1, 4, 7 }, { 1, 4, 7 }, { 0, 6, 10 }, { 0, 6, 10 }, { 0, 4, 8 }, { 0, 4, 8 } }, { { 0, 8, 3 }, { 0, 8, 2 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 5 }, { 0, 8, 4 }, { 0, 8, 8 }, { 0, 8, 8 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 2 }, { 0, 8, 2 }, { 0, 8, 4 }, { 0, 8, 4 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 8 }, { 0, 8, 8 } }, { { 0, 8, 3 }, { 0, 8, 2 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 5 }, { 0, 8, 4 }, { 0, 8, 8 }, { 0, 8, 8 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 2 }, { 0, 8, 2 }, { 0, 8, 4 }, { 0, 8, 4 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 8 }, { 0, 8, 8 } }, { { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 2 }, { 0, 10, 3 }, { 0, 10, 4 }, { 0, 10, 5 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 8 }, { 0, 10, 8 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 2 }, { 0, 10, 2 }, { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 4 }, { 0, 10, 4 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 8 }, { 0, 10, 8 } }, { { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 2 }, { 0, 10, 3 }, { 0, 10, 4 }, { 0, 10, 5 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 8 }, { 0, 10, 8 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 2 }, { 0, 10, 2 }, { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 4 }, { 0, 10, 4 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 8 }, { 0, 10, 8 } }, { { 1, 2, 3 }, { 1, 0, 1 }, { 1, 0, 2 }, { 1, 2, 0 }, { 1, 0, 4 }, { 1, 2, 6 }, { 1, 2, 5 }, { 1, 0, 7 }, { 0, 0, 8 }, { 0, 0, 8 }, { 0, 2, 10 }, { 0, 2, 10 }, { 1, 0, 2 }, { 1, 0, 2 }, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 7 }, { 1, 0, 7 }, { 1, 0, 4 }, { 1, 0, 4 }, { 0, 2, 10 }, { 0, 2, 10 }, { 0, 0, 8 }, { 0, 0, 8 } }, { { 1, 2, 3 }, { 1, 0, 1 }, { 1, 0, 2 }, { 1, 2, 0 }, { 1, 0, 4 }, { 1, 2, 6 }, { 1, 2, 5 }, { 1, 0, 7 }, { 0, 0, 8 }, { 0, 0, 8 }, { 0, 2, 10 }, { 0, 2, 10 }, { 1, 0, 2 }, { 1, 0, 2 }, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 7 }, { 1, 0, 7 }, { 1, 0, 4 }, { 1, 0, 4 }, { 0, 2, 10 }, { 0, 2, 10 }, { 0, 0, 8 }, { 0, 0, 8 } }, { { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 5 }, { 1, 0, 5 }, { 1, 0, 6 }, { 1, 0, 6 }, { 0, 2, 8 }, { 0, 2, 8 }, { 0, 0, 10 }, { 0, 0, 10 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 6 }, { 1, 0, 6 }, { 1, 0, 5 }, { 1, 0, 5 }, { 0, 0, 10 }, { 0, 0, 10 }, { 0, 2, 8 }, { 0, 2, 8 } }, { { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 5 }, { 1, 0, 5 }, { 1, 0, 6 }, { 1, 0, 6 }, { 0, 2, 8 }, { 0, 2, 8 }, { 0, 0, 10 }, { 0, 0, 10 }, { 1, 0, 3 }, { 1, 0, 3 }, { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 6 }, { 1, 0, 6 }, { 1, 0, 5 }, { 1, 0, 5 }, { 0, 0, 10 }, { 0, 0, 10 }, { 0, 2, 8 }, { 0, 2, 8 } }, { { 1, 6, 0 }, { 1, 4, 2 }, { 1, 4, 1 }, { 1, 6, 3 }, { 1, 4, 7 }, { 1, 6, 5 }, { 1, 6, 6 }, { 1, 4, 4 }, { 0, 4, 8 }, { 0, 4, 8 }, { 0, 6, 10 }, { 0, 6, 10 }, { 1, 4, 1 }, { 1, 4, 1 }, { 1, 4, 2 }, { 1, 4, 2 }, { 1, 4, 4 }, { 1, 4, 4 }, { 1, 4, 7 }, { 1, 4, 7 }, { 0, 6, 10 }, { 0, 6, 10 }, { 0, 4, 8 }, { 0, 4, 8 } }, { { 1, 6, 0 }, { 1, 4, 2 }, { 1, 4, 1 }, { 1, 6, 3 }, { 1, 4, 7 }, { 1, 6, 5 }, { 1, 6, 6 }, { 1, 4, 4 }, { 0, 4, 8 }, { 0, 4, 8 }, { 0, 6, 10 }, { 0, 6, 10 }, { 1, 4, 1 }, { 1, 4, 1 }, { 1, 4, 2 }, { 1, 4, 2 }, { 1, 4, 4 }, { 1, 4, 4 }, { 1, 4, 7 }, { 1, 4, 7 }, { 0, 6, 10 }, { 0, 6, 10 }, { 0, 4, 8 }, { 0, 4, 8 } }, { { 1, 4, 3 }, { 1, 4, 3 }, { 1, 4, 0 }, { 1, 4, 0 }, { 1, 4, 6 }, { 1, 4, 6 }, { 1, 4, 5 }, { 1, 4, 5 }, { 0, 6, 8 }, { 0, 6, 8 }, { 0, 4, 10 }, { 0, 4, 10 }, { 1, 4, 0 }, { 1, 4, 0 }, { 1, 4, 3 }, { 1, 4, 3 }, { 1, 4, 5 }, { 1, 4, 5 }, { 1, 4, 6 }, { 1, 4, 6 }, { 0, 4, 10 }, { 0, 4, 10 }, { 0, 6, 8 }, { 0, 6, 8 } }, { { 1, 4, 3 }, { 1, 4, 3 }, { 1, 4, 0 }, { 1, 4, 0 }, { 1, 4, 6 }, { 1, 4, 6 }, { 1, 4, 5 }, { 1, 4, 5 }, { 0, 6, 8 }, { 0, 6, 8 }, { 0, 4, 10 }, { 0, 4, 10 }, { 1, 4, 0 }, { 1, 4, 0 }, { 1, 4, 3 }, { 1, 4, 3 }, { 1, 4, 5 }, { 1, 4, 5 }, { 1, 4, 6 }, { 1, 4, 6 }, { 0, 4, 10 }, { 0, 4, 10 }, { 0, 6, 8 }, { 0, 6, 8 } }, { { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 2 }, { 0, 10, 3 }, { 0, 10, 4 }, { 0, 10, 5 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 8 }, { 0, 10, 8 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 2 }, { 0, 10, 2 }, { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 4 }, { 0, 10, 4 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 8 }, { 0, 10, 8 } }, { { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 2 }, { 0, 10, 3 }, { 0, 10, 4 }, { 0, 10, 5 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 8 }, { 0, 10, 8 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 2 }, { 0, 10, 2 }, { 0, 10, 0 }, { 0, 10, 0 }, { 0, 10, 6 }, { 0, 10, 6 }, { 0, 10, 4 }, { 0, 10, 4 }, { 0, 10, 10 }, { 0, 10, 10 }, { 0, 10, 8 }, { 0, 10, 8 } }, { { 0, 8, 3 }, { 0, 8, 2 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 5 }, { 0, 8, 4 }, { 0, 8, 8 }, { 0, 8, 8 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 2 }, { 0, 8, 2 }, { 0, 8, 4 }, { 0, 8, 4 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 8 }, { 0, 8, 8 } }, { { 0, 8, 3 }, { 0, 8, 2 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 5 }, { 0, 8, 4 }, { 0, 8, 8 }, { 0, 8, 8 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 0 }, { 0, 8, 0 }, { 0, 8, 2 }, { 0, 8, 2 }, { 0, 8, 4 }, { 0, 8, 4 }, { 0, 8, 6 }, { 0, 8, 6 }, { 0, 8, 10 }, { 0, 8, 10 }, { 0, 8, 8 }, { 0, 8, 8 } } }, { { { 0, 0, 0 }, { 0, 3, 0 }, { 0, 3, 2 }, { 0, 0, 3 }, { 0, 3, 4 }, { 0, 0, 5 }, { 0, 0, 6 }, { 0, 3, 6 }, { 1, 5, 23 }, { 1, 5, 22 }, { 1, 5, 21 }, { 1, 5, 20 }, { 0, 5, 2 }, { 0, 6, 2 }, { 0, 5, 0 }, { 0, 6, 0 }, { 0, 6, 6 }, { 0, 5, 6 }, { 0, 6, 4 }, { 0, 5, 4 }, { 1, 5, 10 }, { 1, 5, 11 }, { 1, 5, 8 }, { 1, 5, 9 } }, { { 0, 0, 3 }, { 0, 2, 2 }, { 0, 2, 0 }, { 0, 0, 0 }, { 0, 2, 6 }, { 0, 0, 6 }, { 0, 0, 5 }, { 0, 2, 4 }, { 1, 4, 23 }, { 1, 4, 22 }, { 1, 4, 21 }, { 1, 4, 20 }, { 0, 6, 0 }, { 0, 4, 0 }, { 0, 6, 2 }, { 0, 4, 2 }, { 0, 4, 4 }, { 0, 6, 4 }, { 0, 4, 6 }, { 0, 6, 6 }, { 1, 4, 10 }, { 1, 4, 11 }, { 1, 4, 8 }, { 1, 4, 9 } }, { { 0, 2, 3 }, { 0, 0, 2 }, { 0, 0, 0 }, { 0, 2, 0 }, { 0, 0, 6 }, { 0, 2, 6 }, { 0, 2, 5 }, { 0, 0, 4 }, { 1, 4, 22 }, { 1, 4, 23 }, { 1, 4, 20 }, { 1, 4, 21 }, { 0, 4, 0 }, { 0, 6, 0 }, { 0, 4, 2 }, { 0, 6, 2 }, { 0, 6, 4 }, { 0, 4, 4 }, { 0, 6, 6 }, { 0, 4, 6 }, { 1, 4, 11 }, { 1, 4, 10 }, { 1, 4, 9 }, { 1, 4, 8 } }, { { 0, 3, 0 }, { 0, 0, 0 }, { 0, 0, 2 }, { 0, 3, 3 }, { 0, 0, 4 }, { 0, 3, 5 }, { 0, 3, 6 }, { 0, 0, 6 }, { 1, 5, 22 }, { 1, 5, 23 }, { 1, 5, 20 }, { 1, 5, 21 }, { 0, 6, 2 }, { 0, 5, 2 }, { 0, 6, 0 }, { 0, 5, 0 }, { 0, 5, 6 }, { 0, 6, 6 }, { 0, 5, 4 }, { 0, 6, 4 }, { 1, 5, 11 }, { 1, 5, 10 }, { 1, 5, 9 }, { 1, 5, 8 } }, { { 0, 4, 3 }, { 0, 6, 2 }, { 0, 6, 0 }, { 0, 4, 0 }, { 0, 6, 6 }, { 0, 4, 6 }, { 0, 4, 5 }, { 0, 6, 4 }, { 1, 0, 21 }, { 1, 0, 20 }, { 1, 0, 23 }, { 1, 0, 22 }, { 0, 0, 0 }, { 0, 2, 0 }, { 0, 0, 2 }, { 0, 2, 2 }, { 0, 2, 4 }, { 0, 0, 4 }, { 0, 2, 6 }, { 0, 0, 6 }, { 1, 0, 8 }, { 1, 0, 9 }, { 1, 0, 10 }, { 1, 0, 11 } }, { { 0, 5, 0 }, { 0, 6, 0 }, { 0, 6, 2 }, { 0, 5, 3 }, { 0, 6, 4 }, { 0, 5, 5 }, { 0, 5, 6 }, { 0, 6, 6 }, { 1, 0, 22 }, { 1, 0, 23 }, { 1, 0, 20 }, { 1, 0, 21 }, { 0, 3, 2 }, { 0, 0, 2 }, { 0, 3, 0 }, { 0, 0, 0 }, { 0, 0, 6 }, { 0, 3, 6 }, { 0, 0, 4 }, { 0, 3, 4 }, { 1, 0, 11 }, { 1, 0, 10 }, { 1, 0, 9 }, { 1, 0, 8 } }, { { 0, 6, 0 }, { 0, 5, 0 }, { 0, 5, 2 }, { 0, 6, 3 }, { 0, 5, 4 }, { 0, 6, 5 }, { 0, 6, 6 }, { 0, 5, 6 }, { 1, 0, 23 }, { 1, 0, 22 }, { 1, 0, 21 }, { 1, 0, 20 }, { 0, 0, 2 }, { 0, 3, 2 }, { 0, 0, 0 }, { 0, 3, 0 }, { 0, 3, 6 }, { 0, 0, 6 }, { 0, 3, 4 }, { 0, 0, 4 }, { 1, 0, 10 }, { 1, 0, 11 }, { 1, 0, 8 }, { 1, 0, 9 } }, { { 0, 6, 3 }, { 0, 4, 2 }, { 0, 4, 0 }, { 0, 6, 0 }, { 0, 4, 6 }, { 0, 6, 6 }, { 0, 6, 5 }, { 0, 4, 4 }, { 1, 0, 20 }, { 1, 0, 21 }, { 1, 0, 22 }, { 1, 0, 23 }, { 0, 2, 0 }, { 0, 0, 0 }, { 0, 2, 2 }, { 0, 0, 2 }, { 0, 0, 4 }, { 0, 2, 4 }, { 0, 0, 6 }, { 0, 2, 6 }, { 1, 0, 9 }, { 1, 0, 8 }, { 1, 0, 11 }, { 1, 0, 10 } }, { { 1, 22, 6 }, { 1, 20, 5 }, { 1, 20, 6 }, { 1, 22, 5 }, { 1, 20, 3 }, { 1, 22, 0 }, { 1, 22, 3 }, { 1, 20, 0 }, { 0, 0, 0 }, { 0, 0, 2 }, { 0, 2, 2 }, { 0, 2, 0 }, { 0, 6, 6 }, { 0, 4, 4 }, { 0, 6, 4 }, { 0, 4, 6 }, { 0, 4, 2 }, { 0, 6, 0 }, { 0, 4, 0 }, { 0, 6, 2 }, { 0, 2, 4 }, { 0, 2, 6 }, { 0, 0, 6 }, { 0, 0, 4 } }, { { 1, 22, 5 }, { 1, 20, 6 }, { 1, 20, 5 }, { 1, 22, 6 }, { 1, 20, 0 }, { 1, 22, 3 }, { 1, 22, 0 }, { 1, 20, 3 }, { 0, 2, 0 }, { 0, 2, 2 }, { 0, 0, 2 }, { 0, 0, 0 }, { 0, 4, 6 }, { 0, 6, 4 }, { 0, 4, 4 }, { 0, 6, 6 }, { 0, 6, 2 }, { 0, 4, 0 }, { 0, 6, 0 }, { 0, 4, 2 }, { 0, 0, 4 }, { 0, 0, 6 }, { 0, 2, 6 }, { 0, 2, 4 } }, { { 1, 20, 6 }, { 1, 20, 7 }, { 1, 20, 4 }, { 1, 20, 5 }, { 1, 20, 1 }, { 1, 20, 0 }, { 1, 20, 3 }, { 1, 20, 2 }, { 0, 2, 2 }, { 0, 2, 0 }, { 0, 0, 0 }, { 0, 0, 2 }, { 0, 6, 4 }, { 0, 4, 6 }, { 0, 6, 6 }, { 0, 4, 4 }, { 0, 4, 0 }, { 0, 6, 2 }, { 0, 4, 2 }, { 0, 6, 0 }, { 0, 0, 6 }, { 0, 0, 4 }, { 0, 2, 4 }, { 0, 2, 6 } }, { { 1, 20, 5 }, { 1, 20, 4 }, { 1, 20, 7 }, { 1, 20, 6 }, { 1, 20, 2 }, { 1, 20, 3 }, { 1, 20, 0 }, { 1, 20, 1 }, { 0, 0, 2 }, { 0, 0, 0 }, { 0, 2, 0 }, { 0, 2, 2 }, { 0, 4, 4 }, { 0, 6, 6 }, { 0, 4, 6 }, { 0, 6, 4 }, { 0, 6, 0 }, { 0, 4, 2 }, { 0, 6, 2 }, { 0, 4, 0 }, { 0, 2, 6 }, { 0, 2, 4 }, { 0, 0, 4 }, { 0, 0, 6 } }, { { 0, 2, 5 }, { 0, 0, 6 }, { 0, 0, 4 }, { 0, 2, 6 }, { 0, 0, 0 }, { 0, 2, 3 }, { 0, 2, 0 }, { 0, 0, 2 }, { 0, 6, 6 }, { 0, 6, 4 }, { 0, 4, 6 }, { 0, 4, 4 }, { 1, 16, 18 }, { 1, 16, 19 }, { 1, 16, 16 }, { 1, 16, 17 }, { 1, 16, 12 }, { 1, 16, 13 }, { 1, 16, 14 }, { 1, 16, 15 }, { 0, 4, 2 }, { 0, 4, 0 }, { 0, 6, 2 }, { 0, 6, 0 } }, { { 0, 2, 6 }, { 0, 0, 4 }, { 0, 0, 6 }, { 0, 2, 5 }, { 0, 0, 2 }, { 0, 2, 0 }, { 0, 2, 3 }, { 0, 0, 0 }, { 0, 4, 4 }, { 0, 4, 6 }, { 0, 6, 4 }, { 0, 6, 6 }, { 1, 16, 17 }, { 1, 16, 16 }, { 1, 16, 19 }, { 1, 16, 18 }, { 1, 16, 15 }, { 1, 16, 14 }, { 1, 16, 13 }, { 1, 16, 12 }, { 0, 6, 0 }, { 0, 6, 2 }, { 0, 4, 0 }, { 0, 4, 2 } }, { { 0, 0, 5 }, { 0, 2, 6 }, { 0, 2, 4 }, { 0, 0, 6 }, { 0, 2, 0 }, { 0, 0, 3 }, { 0, 0, 0 }, { 0, 2, 2 }, { 0, 4, 6 }, { 0, 4, 4 }, { 0, 6, 6 }, { 0, 6, 4 }, { 1, 16, 16 }, { 1, 16, 17 }, { 1, 16, 18 }, { 1, 16, 19 }, { 1, 16, 14 }, { 1, 16, 15 }, { 1, 16, 12 }, { 1, 16, 13 }, { 0, 6, 2 }, { 0, 6, 0 }, { 0, 4, 2 }, { 0, 4, 0 } }, { { 0, 0, 6 }, { 0, 2, 4 }, { 0, 2, 6 }, { 0, 0, 5 }, { 0, 2, 2 }, { 0, 0, 0 }, { 0, 0, 3 }, { 0, 2, 0 }, { 0, 6, 4 }, { 0, 6, 6 }, { 0, 4, 4 }, { 0, 4, 6 }, { 1, 16, 19 }, { 1, 16, 18 }, { 1, 16, 17 }, { 1, 16, 16 }, { 1, 16, 13 }, { 1, 16, 12 }, { 1, 16, 15 }, { 1, 16, 14 }, { 0, 4, 0 }, { 0, 4, 2 }, { 0, 6, 0 }, { 0, 6, 2 } }, { { 0, 6, 6 }, { 0, 4, 4 }, { 0, 4, 6 }, { 0, 6, 5 }, { 0, 4, 2 }, { 0, 6, 0 }, { 0, 6, 3 }, { 0, 4, 0 }, { 0, 2, 4 }, { 0, 2, 6 }, { 0, 0, 4 }, { 0, 0, 6 }, { 1, 12, 16 }, { 1, 12, 17 }, { 1, 12, 18 }, { 1, 12, 19 }, { 1, 12, 14 }, { 1, 12, 15 }, { 1, 12, 12 }, { 1, 12, 13 }, { 0, 0, 0 }, { 0, 0, 2 }, { 0, 2, 0 }, { 0, 2, 2 } }, { { 0, 6, 5 }, { 0, 4, 6 }, { 0, 4, 4 }, { 0, 6, 6 }, { 0, 4, 0 }, { 0, 6, 3 }, { 0, 6, 0 }, { 0, 4, 2 }, { 0, 0, 6 }, { 0, 0, 4 }, { 0, 2, 6 }, { 0, 2, 4 }, { 1, 12, 19 }, { 1, 12, 18 }, { 1, 12, 17 }, { 1, 12, 16 }, { 1, 12, 13 }, { 1, 12, 12 }, { 1, 12, 15 }, { 1, 12, 14 }, { 0, 2, 2 }, { 0, 2, 0 }, { 0, 0, 2 }, { 0, 0, 0 } }, { { 0, 4, 6 }, { 0, 6, 4 }, { 0, 6, 6 }, { 0, 4, 5 }, { 0, 6, 2 }, { 0, 4, 0 }, { 0, 4, 3 }, { 0, 6, 0 }, { 0, 0, 4 }, { 0, 0, 6 }, { 0, 2, 4 }, { 0, 2, 6 }, { 1, 12, 18 }, { 1, 12, 19 }, { 1, 12, 16 }, { 1, 12, 17 }, { 1, 12, 12 }, { 1, 12, 13 }, { 1, 12, 14 }, { 1, 12, 15 }, { 0, 2, 0 }, { 0, 2, 2 }, { 0, 0, 0 }, { 0, 0, 2 } }, { { 0, 4, 5 }, { 0, 6, 6 }, { 0, 6, 4 }, { 0, 4, 6 }, { 0, 6, 0 }, { 0, 4, 3 }, { 0, 4, 0 }, { 0, 6, 2 }, { 0, 2, 6 }, { 0, 2, 4 }, { 0, 0, 6 }, { 0, 0, 4 }, { 1, 12, 17 }, { 1, 12, 16 }, { 1, 12, 19 }, { 1, 12, 18 }, { 1, 12, 15 }, { 1, 12, 14 }, { 1, 12, 13 }, { 1, 12, 12 }, { 0, 0, 2 }, { 0, 0, 0 }, { 0, 2, 2 }, { 0, 2, 0 } }, { { 1, 10, 5 }, { 1, 8, 6 }, { 1, 8, 5 }, { 1, 10, 6 }, { 1, 8, 0 }, { 1, 10, 3 }, { 1, 10, 0 }, { 1, 8, 3 }, { 0, 4, 2 }, { 0, 4, 0 }, { 0, 6, 0 }, { 0, 6, 2 }, { 0, 2, 4 }, { 0, 0, 6 }, { 0, 2, 6 }, { 0, 0, 4 }, { 0, 0, 0 }, { 0, 2, 2 }, { 0, 0, 2 }, { 0, 2, 0 }, { 0, 6, 6 }, { 0, 6, 4 }, { 0, 4, 4 }, { 0, 4, 6 } }, { { 1, 10, 6 }, { 1, 8, 5 }, { 1, 8, 6 }, { 1, 10, 5 }, { 1, 8, 3 }, { 1, 10, 0 }, { 1, 10, 3 }, { 1, 8, 0 }, { 0, 6, 2 }, { 0, 6, 0 }, { 0, 4, 0 }, { 0, 4, 2 }, { 0, 0, 4 }, { 0, 2, 6 }, { 0, 0, 6 }, { 0, 2, 4 }, { 0, 2, 0 }, { 0, 0, 2 }, { 0, 2, 2 }, { 0, 0, 0 }, { 0, 4, 6 }, { 0, 4, 4 }, { 0, 6, 4 }, { 0, 6, 6 } }, { { 1, 8, 5 }, { 1, 8, 4 }, { 1, 8, 7 }, { 1, 8, 6 }, { 1, 8, 2 }, { 1, 8, 3 }, { 1, 8, 0 }, { 1, 8, 1 }, { 0, 6, 0 }, { 0, 6, 2 }, { 0, 4, 2 }, { 0, 4, 0 }, { 0, 2, 6 }, { 0, 0, 4 }, { 0, 2, 4 }, { 0, 0, 6 }, { 0, 0, 2 }, { 0, 2, 0 }, { 0, 0, 0 }, { 0, 2, 2 }, { 0, 4, 4 }, { 0, 4, 6 }, { 0, 6, 6 }, { 0, 6, 4 } }, { { 1, 8, 6 }, { 1, 8, 7 }, { 1, 8, 4 }, { 1, 8, 5 }, { 1, 8, 1 }, { 1, 8, 0 }, { 1, 8, 3 }, { 1, 8, 2 }, { 0, 4, 0 }, { 0, 4, 2 }, { 0, 6, 2 }, { 0, 6, 0 }, { 0, 0, 6 }, { 0, 2, 4 }, { 0, 0, 4 }, { 0, 2, 6 }, { 0, 2, 2 }, { 0, 0, 0 }, { 0, 2, 0 }, { 0, 0, 2 }, { 0, 6, 4 }, { 0, 6, 6 }, { 0, 4, 6 }, { 0, 4, 4 } } } };

    /** Array list storing all the qubits, represented as QubitVertex objects. */
    ArrayList<QubitVertex> vertices;

    /**
    *  Creates a register of <i>n</i> qubits, initialized to the state |0&gt;.
    *  A register of size <i>n</i> enables a total of <i>q</i> = 2<sup><i>n</i></sup> 
    *  quantum states as the computational basis.
    *  @param size the number of qubits this register consists of
    */
    public GraphRegister(int size) {
        vertices = new ArrayList<QubitVertex>(size);
        for (int i = 0; i < size; i++) {
            vertices.add(new QubitVertex());
        }
    }

    /** Add an edge to the graph underlying the state.*/
    private void add_edge(int v1, int v2) {
        if (v1 == v2) {
            throw new IllegalArgumentException("Edge with identical vertices " + v1);
        }
        vertices.get(v1).neighbors.add(v2);
        vertices.get(v2).neighbors.add(v1);
    }

    private boolean del_edge(int v1, int v2) {
        vertices.get(v1).neighbors.remove(v2);
        vertices.get(v2).neighbors.remove(v1);
        return true;
    }

    /** Toggle an edge to the graph underlying the state,
    *  i.e., add it if not present, and delete it if present.
    */
    private void toggle_edge(int v1, int v2) {
        if (vertices.get(v1).neighbors.remove(v2)) {
            vertices.get(v2).neighbors.remove(v1);
        } else {
            add_edge(v1, v2);
        }
    }

    /** Toggles the edges between the vertex sets vs1 and vs2.
    *  The function takes extra care not to invert an edge twice. If vs1 and
    *  vs2 are disjunct, this cannot happen and we do not need the function.
    *  If vs1 == v2s, we can do without, too. 
    */
    private void toggle_edges(HashSet<Integer> vs1, HashSet<Integer> vs2) {
        HashSet<Edge> procd_edges = new HashSet<Edge>();
        Edge edge;
        for (int i : vs1) {
            for (int j : vs2) {
                if ((i != j) && !procd_edges.contains(edge = new Edge(i, j))) {
                    procd_edges.add(edge);
                    toggle_edge(i, j);
                }
            }
        }
    }

    /** Measure the bare graph state in the Z basis.*/
    private int graph_Z_measure(int v, int force) {
        int res;
        if (force == -1) {
            res = (Math.random() < .5) ? 0 : 1;
        } else {
            res = force;
        }
        HashSet<Integer> nbg = new HashSet<Integer>(vertices.get(v).neighbors);
        for (int i : nbg) {
            if (del_edge(v, i) && res == 1) {
                vertices.get(i).byprod = vertices.get(i).byprod.multiply(Z);
            }
        }
        if (res == 0) {
            vertices.get(v).byprod = vertices.get(v).byprod.multiply(H);
        } else {
            vertices.get(v).byprod = vertices.get(v).byprod.multiply(X.multiply(H));
        }
        return res;
    }

    /** Measure the bare graph state in the Y basis.*/
    private int graph_Y_measure(int v, int force) {
        int res;
        if (force != 0 && force != 1) {
            res = (Math.random() < .5) ? 0 : 1;
        } else {
            res = force;
        }
        ArrayList<Integer> vnbg = new ArrayList<Integer>(vertices.get(v).neighbors);
        for (int i : vnbg) {
            if (res != 0) {
                vertices.get(i).byprod = vertices.get(i).byprod.multiply(spiZ);
            } else {
                vertices.get(i).byprod = vertices.get(i).byprod.multiply(smiZ);
            }
        }
        vnbg.add(v);
        for (int i = 0; i < vnbg.size(); i++) {
            for (int j = i; j < vnbg.size(); j++) {
                if (vnbg.get(i) != vnbg.get(j)) {
                    toggle_edge(vnbg.get(i), vnbg.get(j));
                }
            }
        }
        if (res == 0) {
            vertices.get(v).byprod = S.multiply(vertices.get(v).byprod);
        } else {
            vertices.get(v).byprod = S.adjoint().multiply(vertices.get(v).byprod);
        }
        return res;
    }

    /** Measure the bare graph state in the X basis.*/
    private int graph_X_measure(int v, int force) {
        if (vertices.get(v).neighbors.size() == 0) {
            return 0;
        }
        int res;
        if (force != 0 && force != 1) {
            res = (Math.random() < .5) ? 0 : 1;
        } else {
            res = force;
        }
        int vb = vertices.get(v).neighbors.iterator().next();
        HashSet<Integer> vn = new HashSet<Integer>(vertices.get(v).neighbors);
        HashSet<Integer> vbn = new HashSet<Integer>(vertices.get(vb).neighbors);
        if (res == 0) {
            vertices.get(vb).byprod = vertices.get(vb).byprod.multiply(spiY);
            for (int i : vertices.get(v).neighbors) {
                if (i != vb && vertices.get(vb).neighbors.contains(i)) {
                    vertices.get(i).byprod = vertices.get(i).byprod.multiply(Z);
                }
            }
        } else {
            vertices.get(vb).byprod = vertices.get(vb).byprod.multiply(smiY);
            vertices.get(v).byprod = Z.multiply(vertices.get(v).byprod);
            for (int i : vertices.get(vb).neighbors) {
                if (i != v && vertices.get(v).neighbors.contains(i)) {
                    vertices.get(i).byprod = vertices.get(i).byprod.multiply(Z);
                }
            }
        }
        toggle_edges(vn, vbn);
        ArrayList<Integer> isc = new ArrayList<Integer>();
        for (int i : vn) {
            if (vbn.contains(i)) {
                isc.add(i);
            }
        }
        for (int i = 0; i < isc.size(); i++) {
            for (int j = i; j < isc.size(); j++) {
                if (!isc.get(i).equals(isc.get(j))) {
                    toggle_edge(isc.get(i), isc.get(j));
                }
            }
        }
        for (int i : vn) {
            if (i != vb) {
                toggle_edge(vb, i);
            }
        }
        return res;
    }

    /** 
    * Do neighborhood inversions to reduce the VOp of vertex v to the identity.
    * 'avoid' is avoided as swapping partner, i.e. the swapping partner will not 
    * be 'avoid' unless this is the only neighbor of v. 
    * @throws IllegalArgumentException if no neighbor is available
    */
    private boolean remove_byprod_op(int v, int avoid) {
        if (vertices.get(v).neighbors.size() == 0) {
            throw new IllegalArgumentException("Isolated vertex.");
        }
        Iterator<Integer> it = vertices.get(v).neighbors.iterator();
        int vb = it.next();
        if (vb == avoid) {
            if (it.hasNext()) {
                vb = it.next();
            }
        }
        String comp = comp_tbl[vertices.get(v).byprod.code];
        for (int pos = comp.length() - 1; pos >= 0; pos--) {
            if (comp.charAt(pos) == 'U') {
                invertNeighborhood(v);
            } else {
                invertNeighborhood(vb);
            }
        }
        return true;
    }

    /** Use the cphase look-up table. This is called by cphase after VOps that do not
    *  commute with the cphase gate have been removed as far as possible. 
    */
    private void cphase_with_table(int v1, int v2) {
        ConnectionInfo ci = getConnectionInfo(v1, v2);
        int op1 = vertices.get(v1).byprod.code;
        int op2 = vertices.get(v2).byprod.code;
        if ((ci.non1) && !vertices.get(v1).byprod.isDiagonal()) throw new RuntimeException("Wrong operator, cphase table cannot be used");
        if ((ci.non2) && !vertices.get(v2).byprod.isDiagonal()) throw new RuntimeException("Wrong operator, cphase table cannot be used");
        if (cphase_tbl[ci.wasEdge ? 1 : 0][op1][op2][0] != 0) {
            add_edge(v1, v2);
        } else {
            del_edge(v1, v2);
        }
        vertices.get(v1).byprod.code = cphase_tbl[ci.wasEdge ? 1 : 0][op1][op2][1];
        vertices.get(v2).byprod.code = cphase_tbl[ci.wasEdge ? 1 : 0][op1][op2][2];
        ci = getConnectionInfo(v1, v2);
    }

    /** Check whether the qubits are connected to each other and to non-operand vertices.*/
    private ConnectionInfo getConnectionInfo(int v1, int v2) {
        ConnectionInfo ci = new ConnectionInfo();
        ci.wasEdge = vertices.get(v1).neighbors.contains(v2);
        if (!ci.wasEdge) {
            ci.non1 = vertices.get(v1).neighbors.size() >= 1;
            ci.non2 = vertices.get(v2).neighbors.size() >= 1;
        } else {
            ci.non1 = vertices.get(v1).neighbors.size() >= 2;
            ci.non2 = vertices.get(v2).neighbors.size() >= 2;
        }
        return ci;
    }

    /** Returns the quantum register (in state vecor representation) 
    * represented by this graph register.
    * @return the register state represented by this graph register state
    */
    public Register getRegister() {
        int q = (1 << vertices.size());
        final double BY_SQRT_Q = 1 / sqrt(q);
        double[] real = new double[q];
        double[] imag = new double[q];
        double[][][][] matrices = new double[vertices.size()][2][2][2];
        double tmp;
        int[] ia = new int[matrices.length];
        int[] ja = new int[matrices.length];
        int rows, cols;
        double[] c = new double[2];
        boolean[] entangleZ = new boolean[q];
        for (int i = 0; i < vertices.size(); i++) {
            matrices[i] = vertices.get(i).getMatrix();
            for (int j : vertices.get(i).neighbors) {
                if (j > i) continue;
                for (int k = 0; k < q; k++) {
                    if ((k & ((1 << i) | (1 << j))) < ((1 << i) | (1 << j))) {
                        continue;
                    }
                    entangleZ[k] = !entangleZ[k];
                }
            }
        }
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                rows = q;
                cols = q;
                for (int k = matrices.length - 1; k >= 0; k--) {
                    rows /= matrices[k].length;
                    cols /= matrices[k][0].length;
                    ia[k] = (i / rows) % matrices[k].length;
                    ja[k] = (j / cols) % matrices[k][0].length;
                }
                c[0] = matrices[0][ia[0]][ja[0]][0];
                c[1] = matrices[0][ia[0]][ja[0]][1];
                for (int k = 1; k < matrices.length; k++) {
                    tmp = c[0];
                    c[0] = matrices[k][ia[k]][ja[k]][0] * c[0] - matrices[k][ia[k]][ja[k]][1] * c[1];
                    c[1] = matrices[k][ia[k]][ja[k]][0] * c[1] + matrices[k][ia[k]][ja[k]][1] * tmp;
                }
                tmp = entangleZ[j] ? -BY_SQRT_Q : BY_SQRT_Q;
                real[i] += c[0] * tmp;
                imag[i] += c[1] * tmp;
            }
        }
        for (int i = 0; i < real.length; i++) {
            if (abs(real[i]) < Register.ACCURACY) real[i] = 0;
            if (abs(imag[i]) < Register.ACCURACY) imag[i] = 0;
        }
        Register register = new Register(vertices.size(), false);
        register.setReal(real);
        register.setImaginary(imag);
        return register;
    }

    /** Apply the specified local (i.e., single-qubit) operator on vertex v. 
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    *  @param operator the local Clifford operator which is applied to the 
    *  vertex operator of qubit vertex v
    */
    void apply(int v, LocalCliffordOperator operator) {
        vertices.get(v).byprod = operator.multiply(vertices.get(v).byprod);
    }

    /** Apply a Hadamard gate on vertex v.
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    */
    public void hadamard(int v) {
        apply(v, H);
    }

    /** Applies a Pauli-<i>X</i>, or "bit flip", on vertex v. 
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    *  @see #bitFlip(int)
    */
    public void xPauli(int v) {
        apply(v, X);
    }

    /** Applies a Pauli-<i>Y</i> on vertex v. 
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    */
    public void yPauli(int v) {
        apply(v, Y);
    }

    /** Applies a Pauli-<i>Z</i>, or "phase flip", on vertex v. 
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    *  @see #phaseFlip(int)
    */
    public void zPauli(int v) {
        apply(v, Z);
    }

    /** Applies an  <i>S</i> gate, or "phase gate", on vertex v. 
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    */
    public void sGate(int v) {
        apply(v, S);
    }

    /** Applies an inverse <i>S</i> gate on vertex v. 
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    *  @see #sGate(int)
    */
    public void inverseSGate(int v) {
        apply(v, spiZ);
    }

    /** Applies a bitflip gate, i.e., a Pauli X, on vertex v. 
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    *  @see #xPauli(int)
    */
    public void bitFlip(int v) {
        apply(v, X);
    }

    /** Apply a phase flip gate (i.e. a Pauli-<i>Z</i>) on vertex v.
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    */
    public void phaseFlip(int v) {
        apply(v, Z);
    }

    /** Apply a phase gate <i>S</i> on qubit v. Phase gate, or phase rotation, 
    *  means the gate <i>S</i> = |0><0| + i |1><1|.
    *  @param v the qubit on which the gate is to be applied
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    */
    public void phaseRot(int v) {
        apply(v, S);
    }

    /** 
    * Does a conditional phase gate c-<i>S</i> between the two qubits.
    * @param v1 the control qubit 
    * (in an <i>n</i> qubit register, v1 = 0, 1, ..., <i>n</i> - 1
    * @param v2 the target qubit
    * (in an <i>n</i> qubit register, v2 = 0, 1, ..., <i>n</i> - 1
    */
    public void cPhase(int v1, int v2) {
        ConnectionInfo ci = getConnectionInfo(v1, v2);
        if (ci.non1) {
            remove_byprod_op(v1, v2);
        }
        ci = getConnectionInfo(v1, v2);
        if (ci.non2) {
            remove_byprod_op(v2, v1);
        }
        ci = getConnectionInfo(v1, v2);
        if (ci.non1 && !vertices.get(v1).byprod.isDiagonal()) {
            remove_byprod_op(v1, v2);
        }
        cphase_with_table(v1, v2);
    }

    /** 
    * Performs a controlled NOT gate between the vertices vc (control) and vt (target).
    * @param vc the control qubit 
    * (in an <i>n</i> qubit register, vc = 0, 1, ..., <i>n</i> - 1)
    * @param vt the target qubit 
    * (in an <i>n</i> qubit register, vc = 0, 1, ..., <i>n</i> - 1)
    */
    public void cNOT(int vc, int vt) {
        hadamard(vt);
        cPhase(vc, vt);
        hadamard(vt);
    }

    /** 
    * Measures qubit v with the Pauli Z gate as basis operator and returns the measured
    * value.
    * @param v the measured qubit 
    * (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    */
    public int measure(int v) {
        return measure(v, Z);
    }

    /** 
    * Measures qubit v in the specified basis operator and returns the measured
    * value.
    * The measurement basis must be a Pauli operator, i.e., has to be equal to 
    * either <i>X</i>, <i>Y</i>, or <i>Z</i>.
    * @param v the measured qubit 
    * (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    * @param basis the Pauli gate which serves as measurement basis 
    */
    public int measure(int v, LocalCliffordOperator basis) {
        return measure(v, basis, -1);
    }

    /** 
    * Measures qubit v in the specified basis operator and returns the measured
    * value.
    * The measurement basis must be a Pauli operator, i.e., has to be equal to 
    * either <i>X</i>, <i>Y</i>, or <i>Z</i>.
    * If you want to know whether the result was 
    * choosen at random or determined by the state, pass a <code>Boolean</code> 
    * object in which this information will be written. If you want to force the result
    * to be a certain value, pass 0 or 1 to 'force'. This only works, if the 
    * result is not determined. If it is, 'force' is ignored.
    * @param v the measured qubit 
    * (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    * @param basis the Pauli gate which serves as measurement basis 
    */
    int measure(int v, LocalCliffordOperator basis, int force) {
        if (basis.code < 1 || basis.code > 3) {
            throw new IllegalArgumentException("Measurement basis is not a Pauli gate");
        }
        if (force < -1 || force > 1) {
            throw new IllegalArgumentException("Parameter force is not -1, 0, or 1: " + force);
        }
        int rp = basis.conjugate(vertices.get(v).byprod.adjoint());
        if (force != -1 && rp == -1) {
            force = force ^ 0x01;
        }
        int res;
        switch(basis.code) {
            case 1:
                res = graph_X_measure(v, force);
                break;
            case 2:
                res = graph_Y_measure(v, force);
                break;
            case 3:
                res = graph_Z_measure(v, force);
                break;
            default:
                throw new IllegalArgumentException("Measurement basis is not a Pauli gate");
        }
        if (rp == -1) {
            res ^= 1;
        } else {
            if (rp != 1) throw new RuntimeException("Illegal phase unequal to 1: " + rp);
        }
        if (vertices.get(v).neighbors.size() > 0) {
            throw new RuntimeException("Vertex " + v + " is still connected to other qubits after its measurement!");
        }
        return res;
    }

    /** Does a neighborhood inversion (i.e., local complementation) about vertex v.
    *  This changes the state's graph representation but not the state itself, as the
    *  necessary correction to the VOps are applied.
    *  @param v the control qubit 
    *  (in an <i>n</i> qubit register, v = 0, 1, ..., <i>n</i> - 1)
    */
    void invertNeighborhood(int v) {
        Integer[] vn = vertices.get(v).neighbors.toArray(new Integer[0]);
        for (int i = 0; i < vn.length; i++) {
            for (int j = i; j < vn.length; j++) {
                if (vn[i] != vn[j]) {
                    toggle_edge(vn[i], vn[j]);
                }
            }
            vertices.get(vn[i]).byprod = vertices.get(vn[i]).byprod.multiply(spiZ.adjoint());
        }
        vertices.get(v).byprod = vertices.get(v).byprod.multiply(smiX.adjoint());
    }

    String print_adj_list() {
        return vertices.toString();
    }

    /** Compares the specified object with this local Clifford operator.
    *  The method returns true if and only if the specified object represents a 
    *  graph register state which is the same than this register.
    *  @param o the specified reference with which to compare
    *  @return <code>true</code> if and only if the specified object
    *  is the same graph register state than this operator
    */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        boolean equal = true;
        GraphRegister r = (GraphRegister) o;
        for (int k = 0; k < vertices.size() && equal; k++) {
            equal &= vertices.get(k).equals(r.vertices.get(k));
        }
        return equal;
    }

    /** Returns the hash code for this graph register state.
    *  @return the hash code for this graph register state
    */
    public int hashCode() {
        int hash = 7;
        for (int k = 0; k < vertices.size(); k++) {
            double[][][] matrix = vertices.get(k).getMatrix();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    hash = 31 * hash + (new Float(matrix[i][j][0])).hashCode();
                    hash = 31 * hash + (new Float(matrix[i][j][1])).hashCode();
                }
            }
        }
        return hash;
    }

    /** Returns a string representation of this register.
    *  It prints its vertices, each one representing a qubit
    *  and being shown with its associated vertex operator and its
    *  vacancy list, i.e., the list of its neighbor vertices
    *  represnting those qubits which are entangled.
    *  @return a string representation of this register
    */
    public String toString() {
        String output = "";
        for (int i = 0; i < vertices.size(); i++) {
            output += "Vertex " + i + ": " + vertices.get(i) + "\n";
        }
        return output;
    }
}

/** This structure is needed only by toggle_edges. */
class Edge {

    int first;

    int second;

    public Edge(int a, int b) {
        if (a < b) {
            first = a;
            second = b;
        } else {
            first = b;
            second = a;
        }
    }

    public int hashCode() {
        return first << 16 ^ second;
    }
}

/** This class is only for internal use for the cphase functions. */
class ConnectionInfo {

    boolean wasEdge;

    boolean non1;

    boolean non2;
}

;
