package org.openscience.cdk.tools;

import org.openscience.cdk.*;
import java.util.*;
import java.io.*;

/**
 *  Tools class with methods for handling molecular graphs
 *
 *@author     steinbeck
 *@created    17. Juni 2001
 */
public class PathTools implements CDKConstants {

    /**
	 *  Description of the Field
	 */
    public static boolean debug = false;

    /**
	 *  Sums up the columns in a 2D int matrix
	 *
	 *@param  apsp  The 2D int matrix
	 *@return       A 1D matrix containing the column sum of the 2D matrix
	 */
    public static int[] getInt2DColumnSum(int[][] apsp) {
        int[] colSum = new int[apsp.length];
        int sum = 0;
        for (int i = 0; i < apsp.length; i++) {
            sum = 0;
            for (int j = 0; j < apsp.length; j++) {
                sum += apsp[i][j];
            }
            colSum[i] = sum;
        }
        return colSum;
    }

    /**
	 *  All-Pairs-Shortest-Path computation based on Floyds algorithm Takes an nxn
	 *  matrix C of edge costs and produces an nxn matrix A of lengths of shortest
	 *  paths.
	 *
	 *@param  C  Description of Parameter
	 *@return    Description of the Returned Value
	 */
    public static int[][] computeFloydAPSP(int C[][]) {
        int i;
        int j;
        int k;
        int n = C.length;
        int[][] A = new int[n][n];
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (C[i][j] == 0) {
                    A[i][j] = 999999999;
                } else {
                    A[i][j] = 1;
                }
            }
        }
        for (i = 0; i < n; i++) {
            A[i][i] = 0;
        }
        for (k = 0; k < n; k++) {
            for (i = 0; i < n; i++) {
                for (j = 0; j < n; j++) {
                    if (A[i][k] + A[k][j] < A[i][j]) {
                        A[i][j] = A[i][k] + A[k][j];
                    }
                }
            }
        }
        return A;
    }

    /**
	 *  All-Pairs-Shortest-Path computation based on Floyds algorithm Takes an nxn
	 *  matrix C of edge costs and produces an nxn matrix A of lengths of shortest
	 *  paths.
	 *
	 *@param  C  Description of Parameter
	 *@return    Description of the Returned Value
	 */
    public static int[][] computeFloydAPSP(double C[][]) {
        int i;
        int j;
        int k;
        int n = C.length;
        int[][] A = new int[n][n];
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (C[i][j] == 0) {
                    A[i][j] = 0;
                } else {
                    A[i][j] = 1;
                }
            }
        }
        return computeFloydAPSP(A);
    }

    /**
	 *  Recursivly perfoms a depth first search in a molecular graphs contained in
	 *  the AtomContainer molecule, starting at the root atom and returning when it
	 *  hits the target atom.
	 *
	 *@param  molecule                                               The
	 *      AtomContainer to be searched
	 *@param  root                                                   The root atom
	 *      to start the search at
	 *@param  target                                                 The target
	 *@param  path                                                   An
	 *      AtomContainer to be filled with the path
	 *@return                                                        true if the
	 *      target atom was found during this function call
	 *@exception  org.openscience.cdk.exception.NoSuchAtomException  Description of
	 *      Exception
	 */
    public static boolean depthFirstTargetSearch(AtomContainer molecule, Atom root, Atom target, AtomContainer path) throws org.openscience.cdk.exception.NoSuchAtomException {
        Bond[] bonds = molecule.getConnectedBonds(root);
        Atom nextAtom = null;
        root.flags[VISITED] = true;
        for (int f = 0; f < bonds.length; f++) {
            nextAtom = bonds[f].getConnectedAtom(root);
            if (!nextAtom.flags[VISITED]) {
                path.addAtom(nextAtom);
                path.addBond(bonds[f]);
                if (nextAtom == target) {
                    return true;
                } else {
                    if (!depthFirstTargetSearch(molecule, nextAtom, target, path)) {
                        path.removeAtom(nextAtom);
                        path.removeBond(bonds[f]);
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 *  Performs a breadthFirstSearch in an AtomContainer starting with a
	 *  particular sphere, which usually consists of one start atom. While
	 *  searching the graph, the method marks each visited atom. It then puts all
	 *  the atoms connected to the atoms in the given sphere into a new vector
	 *  which forms the sphere to search for the next recursive method call. All
	 *  atoms that have been visited are put into a molecule container. This
	 *  breadthFirstSearch does thus find the connected graph for a given start
	 *  atom.
	 *
	 *@param  ac        The AtomContainer to be searched
	 *@param  sphere    A sphere of atoms to start the search with
	 *@param  molecule  A molecule into which all the atoms and bonds are stored
	 *      that are found during search
	 */
    public static void breadthFirstSearch(AtomContainer ac, Vector sphere, Molecule molecule) {
        Atom atom = null;
        Atom nextAtom = null;
        Vector newSphere = new Vector();
        for (int f = 0; f < sphere.size(); f++) {
            atom = (Atom) sphere.elementAt(f);
            molecule.addAtom(atom);
            Bond[] bonds = ac.getConnectedBonds(atom);
            for (int g = 0; g < bonds.length; g++) {
                if (!bonds[g].flags[VISITED]) {
                    molecule.addBond(bonds[g]);
                    bonds[g].flags[VISITED] = true;
                }
                nextAtom = bonds[g].getConnectedAtom(atom);
                if (!nextAtom.flags[VISITED]) {
                    newSphere.addElement(nextAtom);
                    nextAtom.flags[VISITED] = true;
                }
            }
        }
        if (newSphere.size() > 0) {
            breadthFirstSearch(ac, newSphere, molecule);
        }
    }

    /**
	 *  Performs a breadthFirstTargetSearch in an AtomContainer starting with a
	 *  particular sphere, which usually consists of one start atom. While
	 *  searching the graph, the method marks each visited atom. It then puts all
	 *  the atoms connected to the atoms in the given sphere into a new vector
	 *  which forms the sphere to search for the next recursive method call.
	 *  The method keeps track of the sphere count and returns it as soon
	 *  as the target atom is encountered. 
	 *
	 *@param  ac          The AtomContainer in which the path search is to be performed. 
	 *@param  sphere      The sphere of atoms to start with. Usually just the starting atom
	 *@param  target      The target atom to be searched
	 *@param  pathLength  The current path length, incremented and passed in recursive calls. Call this method with "zero".
	 *@param  cutOff      Stop the path search when this cutOff sphere count has been reached
	 *@return             The shortest path between the starting sphere and the target atom
	 */
    public static int breadthFirstTargetSearch(AtomContainer ac, Vector sphere, Atom target, int pathLength, int cutOff) {
        pathLength++;
        if (pathLength > cutOff) {
            return -1;
        }
        Atom atom = null;
        Atom nextAtom = null;
        Vector newSphere = new Vector();
        for (int f = 0; f < sphere.size(); f++) {
            atom = (Atom) sphere.elementAt(f);
            Bond[] bonds = ac.getConnectedBonds(atom);
            for (int g = 0; g < bonds.length; g++) {
                if (!bonds[g].flags[VISITED]) {
                    bonds[g].flags[VISITED] = true;
                }
                nextAtom = bonds[g].getConnectedAtom(atom);
                if (!nextAtom.flags[VISITED]) {
                    if (nextAtom == target) {
                        return pathLength;
                    }
                    newSphere.addElement(nextAtom);
                    nextAtom.flags[VISITED] = true;
                }
            }
        }
        if (newSphere.size() > 0) {
            return breadthFirstTargetSearch(ac, newSphere, target, pathLength, cutOff);
        }
        return -1;
    }
}
