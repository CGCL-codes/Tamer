package VoltIJ.tools.isosurface;

import VoltIJ.math.Triangle;
import VoltIJ.math.Vertex;

/**
 *
 * @author kiss
 */
public class MCImproved {

    /** unsigned char alias */
    byte uchar;

    /** signed char alias */
    byte schar;

    private final int ALLOC_SIZE = 65536;

    protected boolean _originalMC;

    /**< selects wether the algorithm will use the enhanced topologically controlled lookup table or the original MarchingCubes */
    protected boolean _ext_data;

    /**< selects wether to allocate data or use data from another class */
    protected int _size_x;

    /**< width  of the grid */
    protected int _size_y;

    /**< depth  of the grid */
    protected int _size_z;

    /**< height of the grid */
    protected float[] _data;

    /**< implicit function values sampled on the grid */
    protected int[] _x_verts;

    /**< pre-computed vertex indices on the lower horizontal   edge of each cube */
    protected int[] _y_verts;

    /**< pre-computed vertex indices on the lower longitudinal edge of each cube */
    protected int[] _z_verts;

    /**< pre-computed vertex indices on the lower vertical     edge of each cube */
    protected int _nverts;

    /**< number of allocated vertices  in the vertex   buffer */
    protected int _ntrigs;

    /**< number of allocated triangles in the triangle buffer */
    protected int _Nverts;

    /**< size of the vertex   buffer */
    protected int _Ntrigs;

    /**< size of the triangle buffer */
    protected Vertex[] _vertices;

    /**< vertex   buffer */
    protected Triangle[] _triangles;

    /**< triangle buffer */
    protected int _i;

    /**< abscisse of the active cube */
    protected int _j;

    /**< height of the active cube */
    protected int _k;

    /**< ordinate of the active cube */
    protected float[] _cube = new float[8];

    /**< values of the implicit function on the active cube */
    protected byte _lut_entry;

    /**< cube sign representation in [0..255] */
    protected byte _case;

    /**< case of the active cube in [0..15] */
    protected byte _config;

    /**< configuration of the active cube */
    protected byte _subconfig;

    public MCImproved(int size_x, int size_y, int size_z) {
        _originalMC = false;
        _ext_data = false;
        this._size_x = size_x;
        this._size_y = size_y;
        this._size_z = size_z;
        _data = null;
        _x_verts = null;
        _y_verts = null;
        _z_verts = null;
        _nverts = 0;
        _ntrigs = 0;
        _Nverts = 0;
        _Ntrigs = 0;
        _vertices = null;
        _triangles = null;
    }

    /** accesses the number of vertices of the generated mesh */
    public final int nverts() {
        return _nverts;
    }

    /** accesses the number of triangles of the generated mesh */
    public final int ntrigs() {
        return _ntrigs;
    }

    /** accesses a specific vertex of the generated mesh */
    public final Vertex vert(int i) {
        if (i < 0 || i >= _nverts) {
            return null;
        }
        return _vertices[i];
    }

    /** accesses a specific triangle of the generated mesh */
    public final Triangle trig(int i) {
        if (i < 0 || i >= _ntrigs) {
            return null;
        }
        return _triangles[i];
    }

    /** accesses the vertex buffer of the generated mesh */
    public Vertex[] vertices() {
        return _vertices;
    }

    /** accesses the triangle buffer of the generated mesh */
    public Triangle[] triangles() {
        return _triangles;
    }

    /**  accesses the width  of the grid */
    public final int size_x() {
        return _size_x;
    }

    /**  accesses the depth  of the grid */
    public final int size_y() {
        return _size_y;
    }

    /**  accesses the height of the grid */
    public final int size_z() {
        return _size_z;
    }

    /**
     * changes the size of the grid
     * @params size_x width  of the grid
     * @params size_y depth  of the grid
     * @params size_z height of the grid
     */
    public void set_resolution(int size_x, int size_y, int size_z) {
        _size_x = size_x;
        _size_y = size_y;
        _size_z = size_z;
    }

    /**
     * selects wether the algorithm will use the enhanced topologically controlled lookup table or the original MarchingCubes
     * @params originalMC true for the original Marching Cubes
     */
    public void set_method(boolean originalMC) {
        _originalMC = originalMC;
    }

    /**
     * selects to use data from another class
     * @params data is the pointer to the external data, allocated as a size_x*size_y*size_z vector running in x first
     */
    public void set_ext_data(float[] data) {
        if (!_ext_data) {
            _data = null;
        }
        _ext_data = data != null;
        if (_ext_data) {
            _data = data;
        }
    }

    /**
     * selects to allocate data
     */
    public void set_int_data() {
        _ext_data = false;
        _data = null;
    }

    /**
     * accesses a specific cube of the grid
     * @params i abscisse of the cube
     * @params j ordinate of the cube
     * @params k height of the cube
     */
    public final float get_data(int i, int j, int k) {
        return _data[i + j * _size_x + k * _size_x * _size_y];
    }

    /**
     * sets a specific cube of the grid
     * @params val new value for the cube
     * @params i abscisse of the cube
     * @params j ordinate of the cube
     * @params k height of the cube
     */
    public void set_data(float val, int i, int j, int k) {
        _data[i + j * _size_x + k * _size_x * _size_y] = val;
    }

    protected int get_x_vert(int i, int j, int k) {
        return _x_verts[i + j * _size_x + k * _size_x * _size_y];
    }

    /**
     * accesses the pre-computed vertex index on the lower longitudinal edge of a specific cube
     * @params i abscisse of the cube
     * @params j ordinate of the cube
     * @params k height of the cube
     */
    protected int get_y_vert(int i, int j, int k) {
        return _y_verts[i + j * _size_x + k * _size_x * _size_y];
    }

    /**
     * accesses the pre-computed vertex index on the lower vertical edge of a specific cube
     * @params i abscisse of the cube
     * @params j ordinate of the cube
     * @params k height of the cube
     */
    protected int get_z_vert(int i, int j, int k) {
        return _z_verts[i + j * _size_x + k * _size_x * _size_y];
    }

    /**
     * sets the pre-computed vertex index on the lower horizontal edge of a specific cube
     * @params val the index of the new vertex
     * @params i abscisse of the cube
     * @params j ordinate of the cube
     * @params k height of the cube
     */
    protected void set_x_vert(int val, int i, int j, int k) {
        _x_verts[i + j * _size_x + k * _size_x * _size_y] = val;
    }

    /**
     * sets the pre-computed vertex index on the lower longitudinal edge of a specific cube
     * @params val the index of the new vertex
     * @params i abscisse of the cube
     * @params j ordinate of the cube
     * @params k height of the cube
     */
    protected void set_y_vert(int val, int i, int j, int k) {
        _y_verts[i + j * _size_x + k * _size_x * _size_y] = val;
    }

    /**
     * sets the pre-computed vertex index on the lower vertical edge of a specific cube
     * @params val the index of the new vertex
     * @params i abscisse of the cube
     * @params j ordinate of the cube
     * @params k height of the cube
     */
    protected void set_z_vert(int val, int i, int j, int k) {
        _z_verts[i + j * _size_x + k * _size_x * _size_y] = val;
    }

    void print_cube() {
        System.out.println(_cube[0] + "\t" + _cube[1] + "\t" + _cube[2] + "\t" + _cube[3] + "\t" + _cube[4] + "\t" + _cube[5] + "\t" + _cube[6] + "\t" + _cube[7]);
    }

    void run(float iso) {
        compute_intersection_points(iso);
        for (_k = 0; _k < _size_z - 1; _k++) {
            for (_j = 0; _j < _size_y - 1; _j++) {
                for (_i = 0; _i < _size_x - 1; _i++) {
                    _lut_entry = 0;
                    for (int p = 0; p < 8; ++p) {
                        _cube[p] = get_data(_i + ((p ^ (p >> 1)) & 1), _j + ((p >> 1) & 1), _k + ((p >> 2) & 1)) - iso;
                        if (Math.abs(_cube[p]) < .0000001192092896f) {
                            _cube[p] = .0000001192092896f;
                        }
                        if (_cube[p] > 0) {
                            _lut_entry += 1 << p;
                        }
                    }
                    process_cube();
                }
            }
        }
    }

    void init_temps() {
        if (!_ext_data) {
            _data = new float[_size_x * _size_y * _size_z];
        }
        _x_verts = new int[_size_x * _size_y * _size_z];
        _y_verts = new int[_size_x * _size_y * _size_z];
        _z_verts = new int[_size_x * _size_y * _size_z];
    }

    void init_all() {
        init_temps();
        _nverts = _ntrigs = 0;
        _Nverts = _Ntrigs = ALLOC_SIZE;
        _vertices = new Vertex[_Nverts];
        _triangles = new Triangle[_Ntrigs];
    }

    void clean_temps() {
        if (!_ext_data) {
            _data = null;
        }
        _x_verts = null;
        _y_verts = null;
        _z_verts = null;
    }

    void clean_all() {
        clean_temps();
        _vertices = null;
        _triangles = null;
        _nverts = _ntrigs = 0;
        _Nverts = _Ntrigs = 0;
        _size_x = _size_y = _size_z = -1;
    }

    void compute_intersection_points(float iso) {
        for (_k = 0; _k < _size_z; _k++) {
            for (_j = 0; _j < _size_y; _j++) {
                for (_i = 0; _i < _size_x; _i++) {
                    _cube[0] = get_data(_i, _j, _k) - iso;
                    if (_i < _size_x - 1) {
                        _cube[1] = get_data(_i + 1, _j, _k) - iso;
                    } else {
                        _cube[1] = _cube[0];
                    }
                    if (_j < _size_y - 1) {
                        _cube[3] = get_data(_i, _j + 1, _k) - iso;
                    } else {
                        _cube[3] = _cube[0];
                    }
                    if (_k < _size_z - 1) {
                        _cube[4] = get_data(_i, _j, _k + 1) - iso;
                    } else {
                        _cube[4] = _cube[0];
                    }
                    if (Math.abs(_cube[0]) < .0000001192092896f) {
                        _cube[0] = .0000001192092896f;
                    }
                    if (Math.abs(_cube[1]) < .0000001192092896f) {
                        _cube[1] = .0000001192092896f;
                    }
                    if (Math.abs(_cube[3]) < .0000001192092896f) {
                        _cube[3] = .0000001192092896f;
                    }
                    if (Math.abs(_cube[4]) < .0000001192092896f) {
                        _cube[4] = .0000001192092896f;
                    }
                    if (_cube[0] < 0) {
                        if (_cube[1] > 0) {
                            set_x_vert(add_x_vertex(), _i, _j, _k);
                        }
                        if (_cube[3] > 0) {
                            set_y_vert(add_y_vertex(), _i, _j, _k);
                        }
                        if (_cube[4] > 0) {
                            set_z_vert(add_z_vertex(), _i, _j, _k);
                        }
                    } else {
                        if (_cube[1] < 0) {
                            set_x_vert(add_x_vertex(), _i, _j, _k);
                        }
                        if (_cube[3] < 0) {
                            set_y_vert(add_y_vertex(), _i, _j, _k);
                        }
                        if (_cube[4] < 0) {
                            set_z_vert(add_z_vertex(), _i, _j, _k);
                        }
                    }
                }
            }
        }
    }

    boolean test_face(int face) {
        float A, B, C, D;
        switch(face) {
            case -1:
            case 1:
                A = _cube[0];
                B = _cube[4];
                C = _cube[5];
                D = _cube[1];
                break;
            case -2:
            case 2:
                A = _cube[1];
                B = _cube[5];
                C = _cube[6];
                D = _cube[2];
                break;
            case -3:
            case 3:
                A = _cube[2];
                B = _cube[6];
                C = _cube[7];
                D = _cube[3];
                break;
            case -4:
            case 4:
                A = _cube[3];
                B = _cube[7];
                C = _cube[4];
                D = _cube[0];
                break;
            case -5:
            case 5:
                A = _cube[0];
                B = _cube[3];
                C = _cube[2];
                D = _cube[1];
                break;
            case -6:
            case 6:
                A = _cube[4];
                B = _cube[7];
                C = _cube[6];
                D = _cube[5];
                break;
            default:
                System.out.println("Invalid face code " + face);
                print_cube();
                A = B = C = D = 0;
        }
        if (Math.abs(A * C - B * D) < .0000001192092896) {
            return face >= 0;
        }
        return face * A * (A * C - B * D) >= 0;
    }

    boolean test_interior(byte s) {
        float t, At = 0, Bt = 0, Ct = 0, Dt = 0, a, b;
        byte test = 0;
        byte edge = -1;
        switch(_case) {
            case 4:
            case 10:
                a = (_cube[4] - _cube[0]) * (_cube[6] - _cube[2]) - (_cube[7] - _cube[3]) * (_cube[5] - _cube[1]);
                b = _cube[2] * (_cube[4] - _cube[0]) + _cube[0] * (_cube[6] - _cube[2]) - _cube[1] * (_cube[7] - _cube[3]) - _cube[3] * (_cube[5] - _cube[1]);
                t = -b / (2 * a);
                if (t < 0 || t > 1) {
                    return s > 0;
                }
                At = _cube[0] + (_cube[4] - _cube[0]) * t;
                Bt = _cube[3] + (_cube[7] - _cube[3]) * t;
                Ct = _cube[2] + (_cube[6] - _cube[2]) * t;
                Dt = _cube[1] + (_cube[5] - _cube[1]) * t;
                break;
            case 6:
            case 7:
            case 12:
            case 13:
                switch(_case) {
                    case 6:
                        edge = LookUpTableTests.test6[_config][2];
                        break;
                    case 7:
                        edge = LookUpTableTests.test7[_config][4];
                        break;
                    case 12:
                        edge = LookUpTableTests.test12[_config][3];
                        break;
                    case 13:
                        edge = LookUpTable.tiling13_5_1[_config][_subconfig][0];
                        break;
                }
                switch(edge) {
                    case 0:
                        t = _cube[0] / (_cube[0] - _cube[1]);
                        At = 0;
                        Bt = _cube[3] + (_cube[2] - _cube[3]) * t;
                        Ct = _cube[7] + (_cube[6] - _cube[7]) * t;
                        Dt = _cube[4] + (_cube[5] - _cube[4]) * t;
                        break;
                    case 1:
                        t = _cube[1] / (_cube[1] - _cube[2]);
                        At = 0;
                        Bt = _cube[0] + (_cube[3] - _cube[0]) * t;
                        Ct = _cube[4] + (_cube[7] - _cube[4]) * t;
                        Dt = _cube[5] + (_cube[6] - _cube[5]) * t;
                        break;
                    case 2:
                        t = _cube[2] / (_cube[2] - _cube[3]);
                        At = 0;
                        Bt = _cube[1] + (_cube[0] - _cube[1]) * t;
                        Ct = _cube[5] + (_cube[4] - _cube[5]) * t;
                        Dt = _cube[6] + (_cube[7] - _cube[6]) * t;
                        break;
                    case 3:
                        t = _cube[3] / (_cube[3] - _cube[0]);
                        At = 0;
                        Bt = _cube[2] + (_cube[1] - _cube[2]) * t;
                        Ct = _cube[6] + (_cube[5] - _cube[6]) * t;
                        Dt = _cube[7] + (_cube[4] - _cube[7]) * t;
                        break;
                    case 4:
                        t = _cube[4] / (_cube[4] - _cube[5]);
                        At = 0;
                        Bt = _cube[7] + (_cube[6] - _cube[7]) * t;
                        Ct = _cube[3] + (_cube[2] - _cube[3]) * t;
                        Dt = _cube[0] + (_cube[1] - _cube[0]) * t;
                        break;
                    case 5:
                        t = _cube[5] / (_cube[5] - _cube[6]);
                        At = 0;
                        Bt = _cube[4] + (_cube[7] - _cube[4]) * t;
                        Ct = _cube[0] + (_cube[3] - _cube[0]) * t;
                        Dt = _cube[1] + (_cube[2] - _cube[1]) * t;
                        break;
                    case 6:
                        t = _cube[6] / (_cube[6] - _cube[7]);
                        At = 0;
                        Bt = _cube[5] + (_cube[4] - _cube[5]) * t;
                        Ct = _cube[1] + (_cube[0] - _cube[1]) * t;
                        Dt = _cube[2] + (_cube[3] - _cube[2]) * t;
                        break;
                    case 7:
                        t = _cube[7] / (_cube[7] - _cube[4]);
                        At = 0;
                        Bt = _cube[6] + (_cube[5] - _cube[6]) * t;
                        Ct = _cube[2] + (_cube[1] - _cube[2]) * t;
                        Dt = _cube[3] + (_cube[0] - _cube[3]) * t;
                        break;
                    case 8:
                        t = _cube[0] / (_cube[0] - _cube[4]);
                        At = 0;
                        Bt = _cube[3] + (_cube[7] - _cube[3]) * t;
                        Ct = _cube[2] + (_cube[6] - _cube[2]) * t;
                        Dt = _cube[1] + (_cube[5] - _cube[1]) * t;
                        break;
                    case 9:
                        t = _cube[1] / (_cube[1] - _cube[5]);
                        At = 0;
                        Bt = _cube[0] + (_cube[4] - _cube[0]) * t;
                        Ct = _cube[3] + (_cube[7] - _cube[3]) * t;
                        Dt = _cube[2] + (_cube[6] - _cube[2]) * t;
                        break;
                    case 10:
                        t = _cube[2] / (_cube[2] - _cube[6]);
                        At = 0;
                        Bt = _cube[1] + (_cube[5] - _cube[1]) * t;
                        Ct = _cube[0] + (_cube[4] - _cube[0]) * t;
                        Dt = _cube[3] + (_cube[7] - _cube[3]) * t;
                        break;
                    case 11:
                        t = _cube[3] / (_cube[3] - _cube[7]);
                        At = 0;
                        Bt = _cube[2] + (_cube[6] - _cube[2]) * t;
                        Ct = _cube[1] + (_cube[5] - _cube[1]) * t;
                        Dt = _cube[0] + (_cube[4] - _cube[0]) * t;
                        break;
                    default:
                        System.out.println("Invalid edge " + edge);
                        print_cube();
                        break;
                }
                break;
            default:
                System.out.println("Invalid ambiguous case " + _case);
                print_cube();
                break;
        }
        if (At >= 0) {
            test++;
        }
        if (Bt >= 0) {
            test += 2;
        }
        if (Ct >= 0) {
            test += 4;
        }
        if (Dt >= 0) {
            test += 8;
        }
        switch(test) {
            case 0:
                return s > 0;
            case 1:
                return s > 0;
            case 2:
                return s > 0;
            case 3:
                return s > 0;
            case 4:
                return s > 0;
            case 5:
                if (At * Ct - Bt * Dt < .0000001192092896) {
                    return s > 0;
                }
                break;
            case 6:
                return s > 0;
            case 7:
                return s < 0;
            case 8:
                return s > 0;
            case 9:
                return s > 0;
            case 10:
                if (At * Ct - Bt * Dt >= .0000001192092896) {
                    return s > 0;
                }
                break;
            case 11:
                return s < 0;
            case 12:
                return s > 0;
            case 13:
                return s < 0;
            case 14:
                return s < 0;
            case 15:
                return s < 0;
        }
        return s < 0;
    }

    void process_cube() {
        if (_originalMC) {
            int nt = 0;
            while (LookUpTableCases.casesClassic[_lut_entry][3 * nt] != -1) {
                nt++;
            }
            add_triangle(LookUpTableCases.casesClassic[_lut_entry], nt);
            return;
        }
        int v12 = -1;
        _case = LookUpTableCases.cases[_lut_entry][0];
        _config = LookUpTableCases.cases[_lut_entry][1];
        _subconfig = 0;
        switch(_case) {
            case 0:
                break;
            case 1:
                add_triangle(LookUpTableTiling.tiling1[_config], 1);
                break;
            case 2:
                add_triangle(LookUpTableTiling.tiling2[_config], 2);
                break;
            case 3:
                if (test_face(LookUpTableTests.test3[_config])) {
                    add_triangle(LookUpTable.tiling3_2[_config], 4);
                } else {
                    add_triangle(LookUpTable.tiling3_1[_config], 2);
                }
                break;
            case 4:
                if (test_interior(LookUpTableTests.test4[_config])) {
                    add_triangle(LookUpTable.tiling4_1[_config], 2);
                } else {
                    add_triangle(LookUpTable.tiling4_2[_config], 6);
                }
                break;
            case 5:
                add_triangle(LookUpTableTiling.tiling5[_config], 3);
                break;
            case 6:
                if (test_face(LookUpTableTests.test6[_config][0])) {
                    add_triangle(LookUpTable.tiling6_2[_config], 5);
                } else {
                    if (test_interior(LookUpTableTests.test6[_config][1])) {
                        add_triangle(LookUpTable.tiling6_1_1[_config], 3);
                    } else {
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling6_1_2[_config], 9, v12);
                    }
                }
                break;
            case 7:
                if (test_face(LookUpTableTests.test7[_config][0])) {
                    _subconfig += 1;
                }
                if (test_face(LookUpTableTests.test7[_config][1])) {
                    _subconfig += 2;
                }
                if (test_face(LookUpTableTests.test7[_config][2])) {
                    _subconfig += 4;
                }
                switch(_subconfig) {
                    case 0:
                        add_triangle(LookUpTable.tiling7_1[_config], 3);
                        break;
                    case 1:
                        add_triangle(LookUpTable.tiling7_2[_config][0], 5);
                        break;
                    case 2:
                        add_triangle(LookUpTable.tiling7_2[_config][1], 5);
                        break;
                    case 3:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling7_3[_config][0], 9, v12);
                        break;
                    case 4:
                        add_triangle(LookUpTable.tiling7_2[_config][2], 5);
                        break;
                    case 5:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling7_3[_config][1], 9, v12);
                        break;
                    case 6:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling7_3[_config][2], 9, v12);
                        break;
                    case 7:
                        if (test_interior(LookUpTableTests.test7[_config][3])) {
                            add_triangle(LookUpTable.tiling7_4_2[_config], 9);
                        } else {
                            add_triangle(LookUpTable.tiling7_4_1[_config], 5);
                        }
                        break;
                }
                ;
                break;
            case 8:
                add_triangle(LookUpTableTiling.tiling8[_config], 2);
                break;
            case 9:
                add_triangle(LookUpTableTiling.tiling9[_config], 4);
                break;
            case 10:
                if (test_face(LookUpTableTests.test10[_config][0])) {
                    if (test_face(LookUpTableTests.test10[_config][1])) {
                        add_triangle(LookUpTable.tiling10_1_1_[_config], 4);
                    } else {
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling10_2[_config], 8, v12);
                    }
                } else {
                    if (test_face(LookUpTableTests.test10[_config][1])) {
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling10_2_[_config], 8, v12);
                    } else {
                        if (test_interior(LookUpTableTests.test10[_config][2])) {
                            add_triangle(LookUpTable.tiling10_1_1[_config], 4);
                        } else {
                            add_triangle(LookUpTable.tiling10_1_2[_config], 8);
                        }
                    }
                }
                break;
            case 11:
                add_triangle(LookUpTableTiling.tiling11[_config], 4);
                break;
            case 12:
                if (test_face(LookUpTableTests.test12[_config][0])) {
                    if (test_face(LookUpTableTests.test12[_config][1])) {
                        add_triangle(LookUpTable.tiling12_1_1_[_config], 4);
                    } else {
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling12_2[_config], 8, v12);
                    }
                } else {
                    if (test_face(LookUpTableTests.test12[_config][1])) {
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling12_2_[_config], 8, v12);
                    } else {
                        if (test_interior(LookUpTableTests.test12[_config][2])) {
                            add_triangle(LookUpTable.tiling12_1_1[_config], 4);
                        } else {
                            add_triangle(LookUpTable.tiling12_1_2[_config], 8);
                        }
                    }
                }
                break;
            case 13:
                if (test_face(LookUpTableTests.test13[_config][0])) {
                    _subconfig += 1;
                }
                if (test_face(LookUpTableTests.test13[_config][1])) {
                    _subconfig += 2;
                }
                if (test_face(LookUpTableTests.test13[_config][2])) {
                    _subconfig += 4;
                }
                if (test_face(LookUpTableTests.test13[_config][3])) {
                    _subconfig += 8;
                }
                if (test_face(LookUpTableTests.test13[_config][4])) {
                    _subconfig += 16;
                }
                if (test_face(LookUpTableTests.test13[_config][5])) {
                    _subconfig += 32;
                }
                switch(LookUpTableTests.subconfig13[_subconfig]) {
                    case 0:
                        add_triangle(LookUpTable.tiling13_1[_config], 4);
                        break;
                    case 1:
                        add_triangle(LookUpTable.tiling13_2[_config][0], 6);
                        break;
                    case 2:
                        add_triangle(LookUpTable.tiling13_2[_config][1], 6);
                        break;
                    case 3:
                        add_triangle(LookUpTable.tiling13_2[_config][2], 6);
                        break;
                    case 4:
                        add_triangle(LookUpTable.tiling13_2[_config][3], 6);
                        break;
                    case 5:
                        add_triangle(LookUpTable.tiling13_2[_config][4], 6);
                        break;
                    case 6:
                        add_triangle(LookUpTable.tiling13_2[_config][5], 6);
                        break;
                    case 7:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][0], 10, v12);
                        break;
                    case 8:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][1], 10, v12);
                        break;
                    case 9:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][2], 10, v12);
                        break;
                    case 10:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][3], 10, v12);
                        break;
                    case 11:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][4], 10, v12);
                        break;
                    case 12:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][5], 10, v12);
                        break;
                    case 13:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][6], 10, v12);
                        break;
                    case 14:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][7], 10, v12);
                        break;
                    case 15:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][8], 10, v12);
                        break;
                    case 16:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][9], 10, v12);
                        break;
                    case 17:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][10], 10, v12);
                        break;
                    case 18:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3[_config][11], 10, v12);
                        break;
                    case 19:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_4[_config][0], 12, v12);
                        break;
                    case 20:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_4[_config][1], 12, v12);
                        break;
                    case 21:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_4[_config][2], 12, v12);
                        break;
                    case 22:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_4[_config][3], 12, v12);
                        break;
                    case 23:
                        _subconfig = 0;
                        if (test_interior(LookUpTableTests.test13[_config][6])) {
                            add_triangle(LookUpTable.tiling13_5_1[_config][0], 6);
                        } else {
                            add_triangle(LookUpTable.tiling13_5_2[_config][0], 10);
                        }
                        break;
                    case 24:
                        _subconfig = 1;
                        if (test_interior(LookUpTableTests.test13[_config][6])) {
                            add_triangle(LookUpTable.tiling13_5_1[_config][1], 6);
                        } else {
                            add_triangle(LookUpTable.tiling13_5_2[_config][1], 10);
                        }
                        break;
                    case 25:
                        _subconfig = 2;
                        if (test_interior(LookUpTableTests.test13[_config][6])) {
                            add_triangle(LookUpTable.tiling13_5_1[_config][2], 6);
                        } else {
                            add_triangle(LookUpTable.tiling13_5_2[_config][2], 10);
                        }
                        break;
                    case 26:
                        _subconfig = 3;
                        if (test_interior(LookUpTableTests.test13[_config][6])) {
                            add_triangle(LookUpTable.tiling13_5_1[_config][3], 6);
                        } else {
                            add_triangle(LookUpTable.tiling13_5_2[_config][3], 10);
                        }
                        break;
                    case 27:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][0], 10, v12);
                        break;
                    case 28:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][1], 10, v12);
                        break;
                    case 29:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][2], 10, v12);
                        break;
                    case 30:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][3], 10, v12);
                        break;
                    case 31:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][4], 10, v12);
                        break;
                    case 32:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][5], 10, v12);
                        break;
                    case 33:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][6], 10, v12);
                        break;
                    case 34:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][7], 10, v12);
                        break;
                    case 35:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][8], 10, v12);
                        break;
                    case 36:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][9], 10, v12);
                        break;
                    case 37:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][10], 10, v12);
                        break;
                    case 38:
                        v12 = add_c_vertex();
                        add_triangle(LookUpTable.tiling13_3_[_config][11], 10, v12);
                        break;
                    case 39:
                        add_triangle(LookUpTable.tiling13_2_[_config][0], 6);
                        break;
                    case 40:
                        add_triangle(LookUpTable.tiling13_2_[_config][1], 6);
                        break;
                    case 41:
                        add_triangle(LookUpTable.tiling13_2_[_config][2], 6);
                        break;
                    case 42:
                        add_triangle(LookUpTable.tiling13_2_[_config][3], 6);
                        break;
                    case 43:
                        add_triangle(LookUpTable.tiling13_2_[_config][4], 6);
                        break;
                    case 44:
                        add_triangle(LookUpTable.tiling13_2_[_config][5], 6);
                        break;
                    case 45:
                        add_triangle(LookUpTable.tiling13_1_[_config], 4);
                        break;
                    default:
                        System.out.println("Marching Cubes: Impossible case 13?");
                        print_cube();
                }
                break;
            case 14:
                add_triangle(LookUpTableTiling.tiling14[_config], 4);
                break;
        }
    }

    private void add_triangle(byte[] trig, int n) {
        int tv[] = new int[3];
        int v12 = -1;
        for (int t = 0; t < 3 * n; t++) {
            switch(trig[t]) {
                case 0:
                    tv[t % 3] = get_x_vert(_i, _j, _k);
                    break;
                case 1:
                    tv[t % 3] = get_y_vert(_i + 1, _j, _k);
                    break;
                case 2:
                    tv[t % 3] = get_x_vert(_i, _j + 1, _k);
                    break;
                case 3:
                    tv[t % 3] = get_y_vert(_i, _j, _k);
                    break;
                case 4:
                    tv[t % 3] = get_x_vert(_i, _j, _k + 1);
                    break;
                case 5:
                    tv[t % 3] = get_y_vert(_i + 1, _j, _k + 1);
                    break;
                case 6:
                    tv[t % 3] = get_x_vert(_i, _j + 1, _k + 1);
                    break;
                case 7:
                    tv[t % 3] = get_y_vert(_i, _j, _k + 1);
                    break;
                case 8:
                    tv[t % 3] = get_z_vert(_i, _j, _k);
                    break;
                case 9:
                    tv[t % 3] = get_z_vert(_i + 1, _j, _k);
                    break;
                case 10:
                    tv[t % 3] = get_z_vert(_i + 1, _j + 1, _k);
                    break;
                case 11:
                    tv[t % 3] = get_z_vert(_i, _j + 1, _k);
                    break;
                case 12:
                    tv[t % 3] = v12;
                    break;
                default:
                    break;
            }
            if (tv[t % 3] == -1) {
                System.out.println("Marching Cubes: invalid triangle " + _ntrigs + 1);
                print_cube();
            }
            if (t % 3 == 2) {
                if (_ntrigs >= _Ntrigs) {
                    _triangles = new Triangle[2 * _Ntrigs];
                    System.out.println(_Ntrigs + " allocated triangles");
                    _Ntrigs *= 2;
                }
                Triangle T = new Triangle(tv[0], tv[1], tv[2]);
                _triangles[2 * _Ntrigs++] = T;
            }
        }
    }

    private void add_triangle(byte[] trig, int n, int v12) {
        int tv[] = new int[3];
        for (int t = 0; t < 3 * n; t++) {
            switch(trig[t]) {
                case 0:
                    tv[t % 3] = get_x_vert(_i, _j, _k);
                    break;
                case 1:
                    tv[t % 3] = get_y_vert(_i + 1, _j, _k);
                    break;
                case 2:
                    tv[t % 3] = get_x_vert(_i, _j + 1, _k);
                    break;
                case 3:
                    tv[t % 3] = get_y_vert(_i, _j, _k);
                    break;
                case 4:
                    tv[t % 3] = get_x_vert(_i, _j, _k + 1);
                    break;
                case 5:
                    tv[t % 3] = get_y_vert(_i + 1, _j, _k + 1);
                    break;
                case 6:
                    tv[t % 3] = get_x_vert(_i, _j + 1, _k + 1);
                    break;
                case 7:
                    tv[t % 3] = get_y_vert(_i, _j, _k + 1);
                    break;
                case 8:
                    tv[t % 3] = get_z_vert(_i, _j, _k);
                    break;
                case 9:
                    tv[t % 3] = get_z_vert(_i + 1, _j, _k);
                    break;
                case 10:
                    tv[t % 3] = get_z_vert(_i + 1, _j + 1, _k);
                    break;
                case 11:
                    tv[t % 3] = get_z_vert(_i, _j + 1, _k);
                    break;
                case 12:
                    tv[t % 3] = v12;
                    break;
                default:
                    break;
            }
            if (tv[t % 3] == -1) {
                System.out.println("Marching Cubes: invalid triangle " + _ntrigs + 1);
                print_cube();
            }
            if (t % 3 == 2) {
                if (_ntrigs >= _Ntrigs) {
                    Triangle[] temp = _triangles;
                    _triangles = new Triangle[2 * _Ntrigs];
                    System.arraycopy(temp, 0, _triangles, 0, 2 * _Ntrigs);
                    System.out.println(_Ntrigs + " allocated triangles");
                    _Ntrigs *= 2;
                }
                Triangle T = new Triangle(tv[0], tv[1], tv[2]);
                _triangles[2 * _Ntrigs++] = T;
            }
        }
    }

    float get_x_grad(int i, int j, int k) {
        if (i > 0) {
            if (i < _size_x - 1) {
                return (get_data(i + 1, j, k) - get_data(i - 1, j, k)) / 2;
            } else {
                return get_data(i, j, k) - get_data(i - 1, j, k);
            }
        } else {
            return get_data(i + 1, j, k) - get_data(i, j, k);
        }
    }

    float get_y_grad(int i, int j, int k) {
        if (j > 0) {
            if (j < _size_y - 1) {
                return (get_data(i, j + 1, k) - get_data(i, j - 1, k)) / 2;
            } else {
                return get_data(i, j, k) - get_data(i, j - 1, k);
            }
        } else {
            return get_data(i, j + 1, k) - get_data(i, j, k);
        }
    }

    float get_z_grad(int i, int j, int k) {
        if (k > 0) {
            if (k < _size_z - 1) {
                return (get_data(i, j, k + 1) - get_data(i, j, k - 1)) / 2;
            } else {
                return get_data(i, j, k) - get_data(i, j, k - 1);
            }
        } else {
            return get_data(i, j, k + 1) - get_data(i, j, k);
        }
    }

    void test_vertex_addition() {
        if (_nverts >= _Nverts) {
            Vertex[] temp = _vertices;
            _vertices = new Vertex[_Nverts * 2];
            System.arraycopy(temp, 0, _vertices, 0, _Nverts);
            System.out.println(_Nverts + " allocated vertices\n");
            _Nverts *= 2;
        }
    }

    int add_x_vertex() {
        test_vertex_addition();
        Vertex vert = new Vertex();
        float u = (_cube[0]) / (_cube[0] - _cube[1]);
        vert.setX(_i + u);
        vert.setY(_j);
        vert.setZ(_k);
        float nx = (1 - u) * get_x_grad(_i, _j, _k) + u * get_x_grad(_i + 1, _j, _k);
        float ny = (1 - u) * get_y_grad(_i, _j, _k) + u * get_y_grad(_i + 1, _j, _k);
        float nz = (1 - u) * get_z_grad(_i, _j, _k) + u * get_z_grad(_i + 1, _j, _k);
        u = (float) Math.sqrt(vert.getNX() * vert.getNX() + vert.getNY() * vert.getNY() + vert.getNZ() * vert.getNZ());
        if (u > 0) {
            vert.setNX(nx / u);
            vert.setNY(ny / u);
            vert.setNZ(nz / u);
        }
        _vertices[_nverts++] = vert;
        return _nverts - 1;
    }

    int add_y_vertex() {
        test_vertex_addition();
        Vertex vert = new Vertex();
        float u = (_cube[0]) / (_cube[0] - _cube[3]);
        vert.setX(_i);
        vert.setY(_j + u);
        vert.setZ(_k);
        float nx = (1 - u) * get_x_grad(_i, _j, _k) + u * get_x_grad(_i, _j + 1, _k);
        float ny = (1 - u) * get_y_grad(_i, _j, _k) + u * get_y_grad(_i, _j + 1, _k);
        float nz = (1 - u) * get_z_grad(_i, _j, _k) + u * get_z_grad(_i, _j + 1, _k);
        u = (float) Math.sqrt(vert.getNX() * vert.getNX() + vert.getNY() * vert.getNY() + vert.getNZ() * vert.getNZ());
        if (u > 0) {
            vert.setNX(nx / u);
            vert.setNY(ny / u);
            vert.setNZ(nz / u);
        }
        _vertices[_nverts++] = vert;
        return _nverts - 1;
    }

    int add_z_vertex() {
        test_vertex_addition();
        Vertex vert = new Vertex();
        float u = (_cube[0]) / (_cube[0] - _cube[4]);
        vert.setX(_i);
        vert.setY(_j);
        vert.setZ(_k + u);
        float nx = (1 - u) * get_x_grad(_i, _j, _k) + u * get_x_grad(_i, _j, _k + 1);
        float ny = (1 - u) * get_y_grad(_i, _j, _k) + u * get_y_grad(_i, _j, _k + 1);
        float nz = (1 - u) * get_z_grad(_i, _j, _k) + u * get_z_grad(_i, _j, _k + 1);
        u = (float) Math.sqrt(vert.getNX() * vert.getNX() + vert.getNY() * vert.getNY() + vert.getNZ() * vert.getNZ());
        if (u > 0) {
            vert.setNX(nx / u);
            vert.setNY(ny / u);
            vert.setNZ(nz / u);
        }
        _vertices[_nverts++] = vert;
        return _nverts - 1;
    }

    int add_c_vertex() {
        test_vertex_addition();
        Vertex vert = new Vertex();
        float u = 0;
        int vid;
        vid = get_x_vert(_i, _j, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_y_vert(_i + 1, _j, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_x_vert(_i, _j + 1, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_y_vert(_i, _j, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_x_vert(_i, _j, _k + 1);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_y_vert(_i + 1, _j, _k + 1);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_x_vert(_i, _j + 1, _k + 1);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_y_vert(_i, _j, _k + 1);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_z_vert(_i, _j, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_z_vert(_i + 1, _j, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_z_vert(_i + 1, _j + 1, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vid = get_z_vert(_i, _j + 1, _k);
        if (vid != -1) {
            ++u;
            Vertex v = _vertices[vid];
            vert.setX(vert.getX() + v.getX());
            vert.setY(vert.getY() + v.getY());
            vert.setZ(vert.getZ() + v.getZ());
            vert.setNX(vert.getNX() + v.getNX());
            vert.setNY(vert.getNY() + v.getNY());
            vert.setNZ(vert.getNZ() + v.getNZ());
        }
        vert.setX(vert.getX() / u);
        vert.setY(vert.getY() / u);
        vert.setZ(vert.getZ() / u);
        u = (float) Math.sqrt(vert.getNX() * vert.getNX() + vert.getNY() * vert.getNY() + vert.getNZ() * vert.getNZ());
        if (u > 0) {
            vert.setNX(vert.getNX() / u);
            vert.setNY(vert.getNY() / u);
            vert.setNZ(vert.getNZ() / u);
        }
        _vertices[_nverts++] = vert;
        return _nverts - 1;
    }
}
