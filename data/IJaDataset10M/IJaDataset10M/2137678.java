package edu.uta.futureye.tutorial;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import edu.uta.futureye.algebra.SchurComplementStokesSolver;
import edu.uta.futureye.algebra.SpaceVector;
import edu.uta.futureye.algebra.SparseVector;
import edu.uta.futureye.algebra.intf.BlockMatrix;
import edu.uta.futureye.algebra.intf.BlockVector;
import edu.uta.futureye.algebra.intf.Matrix;
import edu.uta.futureye.algebra.intf.Vector;
import edu.uta.futureye.application.DataReader;
import edu.uta.futureye.core.EdgeLocal;
import edu.uta.futureye.core.Element;
import edu.uta.futureye.core.Mesh;
import edu.uta.futureye.core.Node;
import edu.uta.futureye.core.NodeLocal;
import edu.uta.futureye.core.NodeType;
import edu.uta.futureye.core.Vertex;
import edu.uta.futureye.function.AbstractFunction;
import edu.uta.futureye.function.Variable;
import edu.uta.futureye.function.basic.FC;
import edu.uta.futureye.function.basic.SpaceVectorFunction;
import edu.uta.futureye.function.basic.Vector2Function;
import edu.uta.futureye.function.intf.Function;
import edu.uta.futureye.function.intf.VectorFunction;
import edu.uta.futureye.io.MeshReader;
import edu.uta.futureye.lib.assembler.AssemblerVector;
import edu.uta.futureye.lib.element.FEBilinearV_ConstantP;
import edu.uta.futureye.lib.element.FEQuadraticV_ConstantP;
import edu.uta.futureye.lib.element.FEQuadraticV_LinearP;
import edu.uta.futureye.lib.element.FiniteElementType;
import edu.uta.futureye.lib.weakform.WeakFormNavierStokes2D;
import edu.uta.futureye.util.Constant;
import edu.uta.futureye.util.FutureyeException;
import edu.uta.futureye.util.container.ElementList;
import edu.uta.futureye.util.container.NodeList;
import edu.uta.futureye.util.container.ObjIndex;
import edu.uta.futureye.util.container.ObjList;

/**
 * Problem: 2D Navier-Stokes, flow around a cylinder
 * 
 * Ref.
 * 1. Alexander N. BROOKS Streamline Upwind/Petrow-Galerkin Formulations for Convection Dominated
 *    Flows With Particular Emphasis on the Incompressible Navier-Stokes Equations
 *    
 * 2. M. Schafer and S. Turek Benchmark Computations of Laminar Flow Arond a Cylinder
 * @author liuyueming
 *
 */
public class NavierStokesCylinder {

    protected String outputFolder = "tutorial/NavierStokesCylinder";

    protected String file = null;

    protected Mesh mesh = null;

    protected Mesh meshOld = null;

    protected WeakFormNavierStokes2D weakForm = new WeakFormNavierStokes2D();

    protected AssemblerVector assembler = null;

    protected VectorFunction diri = null;

    protected VectorFunction U = new SpaceVectorFunction(2);

    protected double dt = 0.02;

    protected double mu = 0.0005;

    FiniteElementType fe = null;

    int maxNonlinearIter = 30;

    double nonlinearError = 1e-2;

    int maxTimeStep = 1000;

    /**
	 * 
	 * @param testCaseNo
	 */
    public void init(int testCaseNo) {
        if (testCaseNo == 1 || testCaseNo == 2) {
            file = "benchmark_cylinder2";
        } else if (testCaseNo == 3) {
            file = "benchmark_cylinder3";
        } else {
            throw new FutureyeException("testCaseNo should be 1,2,3");
        }
        MeshReader reader = new MeshReader(file + ".grd");
        MeshReader reader2 = new MeshReader(file + ".grd");
        mesh = reader.read2DMesh();
        meshOld = reader2.read2DMesh();
        mesh.nVertex = mesh.getNodeList().size();
        if (testCaseNo == 1 || testCaseNo == 2) {
            for (int i = 1; i <= mesh.getElementList().size(); i++) {
                Element e = mesh.getElementList().at(i);
                e.adjustVerticeToCounterClockwise();
                ObjList<EdgeLocal> edges = e.edges();
                int nNode = e.nodes.size();
                for (int j = 1; j <= edges.size(); j++) {
                    EdgeLocal edge = edges.at(j);
                    Vertex l = edge.beginVertex();
                    Vertex r = edge.endVertex();
                    double cx = (l.coord(1) + r.coord(1)) / 2.0;
                    double cy = (l.coord(2) + r.coord(2)) / 2.0;
                    Node node = new Node(mesh.getNodeList().size() + 1, cx, cy);
                    Node findNode = mesh.containNode(node);
                    if (findNode == null) {
                        edge.addEdgeNode(new NodeLocal(++nNode, node));
                        mesh.addNode(node);
                    } else {
                        edge.addEdgeNode(new NodeLocal(++nNode, findNode));
                    }
                }
                e.applyChange();
            }
        }
        mesh.computeNodeBelongsToElements();
        ElementList eList = mesh.getElementList();
        NodeList nodes = mesh.getNodeList();
        HashMap<NodeType, Function> mapNTF_u = new HashMap<NodeType, Function>();
        mapNTF_u.put(NodeType.Dirichlet, new AbstractFunction("x", "y") {

            @Override
            public double value(Variable v) {
                double x = v.get("x");
                double y = v.get("y");
                double redius = 0.05;
                double H = 0.41;
                if ((y < Constant.meshEps || Math.abs(y - H) < Constant.meshEps) && (x < 2.15)) return 1;
                if (x < Constant.meshEps) return 1;
                if (Math.sqrt((x - 0.2) * (x - 0.2) + (y - 0.2) * (y - 0.2)) <= redius + Constant.meshEps) return 1;
                return 0;
            }
        });
        mapNTF_u.put(NodeType.Neumann, null);
        HashMap<NodeType, Function> mapNTF_p = new HashMap<NodeType, Function>();
        mapNTF_p.put(NodeType.Dirichlet, new AbstractFunction("x", "y") {

            @Override
            public double value(Variable v) {
                double x = v.get("x");
                if (Math.abs(x - 2.2) < Constant.meshEps) return 1;
                return 0;
            }
        });
        mapNTF_p.put(NodeType.Neumann, null);
        mesh.markBorderNode(1, mapNTF_u);
        mesh.markBorderNode(2, mapNTF_u);
        mesh.markBorderNode(3, mapNTF_p);
        mesh.writeNodesInfo(String.format("./%s/%s__MeshInfo_u.dat", outputFolder, file), 1);
        mesh.writeNodesInfo(String.format("./%s/%s__MeshInfo_v.dat", outputFolder, file), 2);
        mesh.writeNodesInfo(String.format("./%s/%s__MeshInfo_p.dat", outputFolder, file), 3);
        if (testCaseNo == 1) fe = new FEQuadraticV_LinearP(); else if (testCaseNo == 2) fe = new FEQuadraticV_ConstantP(); else if (testCaseNo == 3) fe = new FEBilinearV_ConstantP();
        fe.initDOFIndexGenerator(mesh);
        for (int i = 1; i <= eList.size(); i++) {
            fe.assignTo(eList.at(i));
        }
    }

    /**
	 * u_max=0.3 at left side boundary, other u=v=0
	 * p=0 at right side boundary
	 * 
	 * Result Re=40
	 */
    public void imposeDirichletCondition1() {
        diri = new SpaceVectorFunction(3);
        diri.set(1, new AbstractFunction("x", "y") {

            @Override
            public double value(Variable v) {
                double x = v.get("x");
                double y = v.get("y");
                double H = 0.41;
                double Um = 0.3;
                if (x < Constant.meshEps) return 4 * Um * y * (H - y) / (H * H); else return 0.0;
            }
        });
        diri.set(2, FC.c0);
        diri.set(3, FC.c0);
    }

    /**
	 * u_max=1.5 at left side boundary, other u=v=0
	 * p=0 at right side boundary
	 * 
	 * Result Re=100
	 */
    public void imposeDirichletCondition2() {
        diri = new SpaceVectorFunction(3);
        diri.set(1, new AbstractFunction("x", "y") {

            @Override
            public double value(Variable v) {
                double x = v.get("x");
                double y = v.get("y");
                double H = 0.41;
                double Um = 1.5;
                if (x < Constant.meshEps) return 4 * Um * y * (H - y) / (H * H); else return 0.0;
            }
        });
        diri.set(2, FC.c0);
        diri.set(3, FC.c0);
    }

    /**
	 * u_max=1.5 at left side boundary, u=1 at upside and down side, other u=v=0
	 * p=0 at right side boundary
	 * 
	 * Result Re=100
	 */
    public void imposeDirichletCondition3() {
        diri = new SpaceVectorFunction(3);
        diri.set(1, new AbstractFunction("x", "y") {

            @Override
            public double value(Variable v) {
                double x = v.get("x");
                double y = v.get("y");
                double H = 0.41;
                double Um = 1.5;
                if (y < Constant.meshEps || Math.abs(y - 0.41) < Constant.meshEps) return 1;
                if (x < Constant.meshEps) return 4 * Um * y * (H - y) / (H * H); else return 0.0;
            }
        });
        diri.set(2, FC.c0);
        diri.set(3, FC.c0);
    }

    public BlockVector nonlinearIter(int time, int nIter, SpaceVectorFunction uk) {
        if (time == 0) weakForm.setF(new SpaceVectorFunction(FC.c0, FC.c0)); else weakForm.setF(new SpaceVectorFunction(uk.get(1).D(dt), uk.get(2).D(dt)));
        weakForm.setParam(FC.c(mu), U, FC.c(1.0 / dt));
        assembler = new AssemblerVector(mesh, weakForm, fe);
        System.out.println("Begin Assemble...");
        assembler.assemble();
        BlockMatrix stiff = (BlockMatrix) assembler.getStiffnessMatrix();
        BlockVector load = (BlockVector) assembler.getLoadVector();
        assembler.imposeDirichletCondition(diri);
        System.out.println("Assemble done!");
        SchurComplementStokesSolver solver = new SchurComplementStokesSolver(stiff, load);
        return solver.solve2D();
    }

    public BlockVector nonlinearIterSteady(int nIter, SpaceVectorFunction uk) {
        weakForm.setF(new SpaceVectorFunction(FC.c0, FC.c0));
        weakForm.setParam(FC.c(mu), U, FC.c0);
        assembler = new AssemblerVector(mesh, weakForm, fe);
        System.out.println("Begin Assemble...");
        assembler.assemble();
        BlockMatrix stiff = (BlockMatrix) assembler.getStiffnessMatrix();
        BlockVector load = (BlockVector) assembler.getLoadVector();
        assembler.imposeDirichletCondition(diri);
        System.out.println("Assemble done!");
        SchurComplementStokesSolver solver = new SchurComplementStokesSolver(stiff, load);
        return solver.solve2D();
    }

    public void run(int startTimeStep, int testCaseNo, boolean bSteady) {
        init(testCaseNo);
        if (!bSteady && startTimeStep > 0) {
            Vector vecU = DataReader.readVector(String.format("./%s/%s_uv_final_t%02d.dat", outputFolder, file, startTimeStep), 3);
            Vector vecV = DataReader.readVector(String.format("./%s/%s_uv_final_t%02d.dat", outputFolder, file, startTimeStep), 4);
            U.set(1, new Vector2Function(vecU));
            U.set(2, new Vector2Function(vecV));
        } else if (bSteady && startTimeStep > 0) {
            Vector vecU = DataReader.readVector(String.format("./%s/%s_uv_steady_%02d.dat", outputFolder, file, startTimeStep), 3);
            Vector vecV = DataReader.readVector(String.format("./%s/%s_uv_steady_%02d.dat", outputFolder, file, startTimeStep), 4);
            U.set(1, new Vector2Function(vecU));
            U.set(2, new Vector2Function(vecV));
        } else {
            U.set(1, FC.c0);
            U.set(2, FC.c0);
        }
        BlockVector u = null;
        if (bSteady) System.out.println(">>>>>>>>>>>>>>>>>>>steady");
        SpaceVectorFunction uk = new SpaceVectorFunction(2);
        for (int time = startTimeStep + 1; time < this.maxTimeStep; time++) {
            if (!bSteady) System.out.println(">>>>>>>>>>>>>>>>>>>time=" + time);
            uk.set(1, U.get(1));
            uk.set(2, U.get(2));
            for (int iter = 0; iter < this.maxNonlinearIter; iter++) {
                if (bSteady) u = nonlinearIterSteady(iter, uk); else u = nonlinearIter(time, iter, uk);
                int dim = u.getBlock(1).getDim();
                SparseVector delta_u = new SparseVector(dim);
                for (int i = 1; i <= dim; i++) delta_u.set(i, u.getBlock(1).get(i) - U.get(1).value(new Variable().setIndex(i)));
                U.set(1, new Vector2Function(u.getBlock(1)));
                U.set(2, new Vector2Function(u.getBlock(2)));
                System.out.println("Iter=" + iter + " Error Norm2 (||u1_k+1 - u1_k||) = " + delta_u.norm2());
                if (delta_u.norm2() < this.nonlinearError) {
                    String s = "_t%03d";
                    if (bSteady) s = "_steady";
                    Tools.plotVector(mesh, outputFolder, String.format("%s_uv_final" + s + ".dat", file, time), u.getBlock(1), u.getBlock(2));
                    Tools.plotVector(meshOld, outputFolder, String.format("%s_p_final" + s + ".dat", file, time), Tools.valueOnElement2Node(mesh, u.getBlock(3)));
                    if (bSteady) return; else break;
                } else {
                    if (bSteady) {
                        Tools.plotVector(mesh, outputFolder, String.format("%s_uv_steady_%02d.dat", file, time + iter), u.getBlock(1), u.getBlock(2));
                        Tools.plotVector(meshOld, outputFolder, String.format("%s_p_steady_%02d.dat", file, time + iter), Tools.valueOnElement2Node(mesh, u.getBlock(3)));
                    } else {
                        Tools.plotVector(mesh, outputFolder, String.format("%s_uv%02d_%02d.dat", file, time, iter), u.getBlock(1), u.getBlock(2));
                        Tools.plotVector(meshOld, outputFolder, String.format("%s_p%02d_%02d.dat", file, time, iter), Tools.valueOnElement2Node(mesh, u.getBlock(3)));
                    }
                }
            }
            if (bSteady) return;
        }
    }

    public void computeKeyValues(int step, Vector vecU, Vector vecV, Vector vecP) {
        final double[] cylinderCenter = { 0.2, 0.2 };
        double cylinderRadius = 0.05;
        Node frontPoint = new Node(0, 0.1395858728, 0.2002194199);
        Node endPoint = new Node(0, 0.2604143184, 0.1998722827);
        final SpaceVector v1 = new SpaceVector(2);
        final SpaceVector v2 = new SpaceVector(2);
        v1.set(1, frontPoint.coord(1) - cylinderCenter[0]);
        v1.set(2, frontPoint.coord(2) - cylinderCenter[1]);
        NodeList nodes = mesh.getNodeList();
        int N = nodes.size();
        NodeList nodesOnCylinder = new NodeList();
        for (int i = 1; i <= N; i++) {
            Node node = nodes.at(i);
            double x = node.coord(1);
            double y = node.coord(2);
            v2.set(1, x - cylinderCenter[0]);
            v2.set(2, y - cylinderCenter[1]);
            if (0.067 - v2.norm2() > 0 && v2.norm2() - 0.058 > 0) {
                nodesOnCylinder.add(node);
                if (node.coordEquals(frontPoint)) {
                    frontPoint.globalIndex = node.globalIndex;
                }
                if (node.coordEquals(endPoint)) {
                    endPoint.globalIndex = node.globalIndex;
                }
            }
        }
        List<Node> l = nodesOnCylinder.toList();
        Collections.sort(l, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                v2.set(1, o1.coord(1) - cylinderCenter[0]);
                v2.set(2, o1.coord(2) - cylinderCenter[1]);
                double dot1 = v1.dot(v2);
                v2.set(1, o2.coord(1) - cylinderCenter[0]);
                v2.set(2, o2.coord(2) - cylinderCenter[1]);
                double dot2 = v1.dot(v2);
                if (o1.coord(2) >= cylinderCenter[1] && o2.coord(2) >= cylinderCenter[1]) {
                    return (dot1 - dot2) > 0 ? -1 : 1;
                } else if (o1.coord(2) >= cylinderCenter[1] && o2.coord(2) < cylinderCenter[1]) {
                    return -1;
                } else if (o1.coord(2) < cylinderCenter[1] && o2.coord(2) >= cylinderCenter[1]) {
                    return 1;
                } else {
                    return (dot1 - dot2) > 0 ? 1 : -1;
                }
            }
        });
        double U = 1.5;
        double Ubar = 2.0 * U / 3.0;
        double rho = 1.0;
        double mu = 0.001;
        double D = 0.1;
        Node last_node = null;
        double FD = 0.0;
        double FL = 0.0;
        for (int i = 1; i <= nodesOnCylinder.size() + 1; i++) {
            int index = i % nodesOnCylinder.size();
            if (index == 0) index = nodesOnCylinder.size();
            Node node = nodesOnCylinder.at(index);
            v1.set(1, node.coord(1) - cylinderCenter[0]);
            v1.set(2, node.coord(2) - cylinderCenter[1]);
            double nx = v1.get(1) / v1.norm2();
            double ny = v1.get(2) / v1.norm2();
            double tx = ny;
            double ty = -nx;
            double u = vecU.get(node.globalIndex);
            double v = vecV.get(node.globalIndex);
            double vtLen = u * tx + v * ty;
            double vtx = vtLen * tx;
            double vty = vtLen * ty;
            if (i >= 2) {
                double dSx = node.coord(1) - last_node.coord(1);
                double dSy = node.coord(2) - last_node.coord(2);
                double dS = Math.sqrt(dSx * dSx + dSy * dSy);
                double dn = v1.norm2() - cylinderRadius;
                double dvtx = vtx / dn;
                double dvty = vty / dn;
                double P = vecP.get(node.globalIndex);
                double vt_n = Math.sqrt(dvtx * dvtx + dvty * dvty);
                FD += (rho * mu * vt_n * ny - P * nx) * dS;
                FL += -(rho * mu * vt_n * nx + P * ny) * dS;
            }
            last_node = node;
        }
        double cD = 2 * FD / (rho * Ubar * Ubar * D);
        double cL = 2 * FL / (rho * Ubar * Ubar * D);
        double f = 1.0 / (9.0 * 0.05);
        double St = D * f / Ubar;
        double DeltaP = vecP.get(frontPoint.globalIndex) - vecP.get(endPoint.globalIndex);
        System.out.println(String.format("%d\t%f\t%f\t%f\t%f", step, cD, cL, St, DeltaP));
    }

    public void computeKeyValues(int step) {
        Vector vecU = DataReader.readVector(String.format("./%s/%s_uv_final_t%02d.dat", outputFolder, file, step), 3);
        Vector vecV = DataReader.readVector(String.format("./%s/%s_uv_final_t%02d.dat", outputFolder, file, step), 4);
        Vector vecP = DataReader.readVector(String.format("./%s/%s_p_final_t%02d.dat", outputFolder, file, step));
        computeKeyValues(step, vecU, vecV, vecP);
    }

    /**
	 * args[0]: test case number
	 * args[1]: is steady
	 * args[2]: delta t
	 * args[3]: start time step
	 * args[4]: max time step

	 * @param args
	 */
    public static void main(String[] args) {
        NavierStokesCylinder NS = new NavierStokesCylinder();
        int testCaseNo = 3;
        boolean bSteady = false;
        NS.dt = 0.02;
        int startTimeStep = 0;
        if (args.length >= 1) testCaseNo = Integer.parseInt(args[0]);
        if (args.length >= 2) bSteady = Boolean.parseBoolean(args[1]);
        if (args.length >= 3) NS.dt = Double.parseDouble(args[2]);
        if (args.length >= 4) startTimeStep = Integer.parseInt(args[3]);
        if (args.length >= 5) NS.maxTimeStep = Integer.parseInt(args[4]);
        System.out.println("testCaseNo=" + testCaseNo);
        System.out.println("bSteady=" + bSteady);
        System.out.println("dt=" + NS.dt);
        System.out.println("startTimeStep=" + startTimeStep);
        System.out.println("maxTimeStep=" + NS.maxTimeStep);
        System.out.println("------default values------");
        System.out.println("mu=" + NS.mu);
        System.out.println("maxNonlinearIter=" + NS.maxNonlinearIter);
        System.out.println("nonlinearError=" + NS.nonlinearError);
        NS.imposeDirichletCondition3();
        NS.run(startTimeStep, testCaseNo, bSteady);
    }
}
