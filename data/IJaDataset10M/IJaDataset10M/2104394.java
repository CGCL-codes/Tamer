package org.fudaa.ebli.all;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URL;

/**
 * Author: Daeron Meyer Copyright (c) 1995 by The Geometry Center, University of Minnesota Distributed under the terms
 * of the GNU Library General Public License 12-14-95 class Face: your basic storage class for polygonal face
 * information.
 *
 * @version $Revision: 1.3 $ $Date: 2006-10-19 14:13:24 $ by $Author: deniger $
 * @author Guillaume Desnoix
 */
class Face {

    public int nverts_;

    public int[] index_;

    public int cr_;

    public int cg_;

    public int cb_;

    public int zdepth_;

    Face() {
        nverts_ = 0;
        index_ = null;
        cr_ = 255;
        cg_ = 255;
        cb_ = 255;
    }

    Face(final int _nv) {
        nverts_ = _nv;
        index_ = new int[_nv];
        cr_ = 255;
        cg_ = 255;
        cb_ = 255;
    }

    public void numVerts(final int _nv) {
        nverts_ = _nv;
        index_ = new int[_nv];
    }
}

/**
 * class OOGL_OFF: a class for parsing, storing and rendering OFF 3D models. For more information about OFF files and
 * other OOGL (Object Oriented Graphics Library) file formats, check the following URL:
 * http://www.geom.umn.edu/software/geomview/docs/oogltour.html
 */
public class OOGLOFF {

    Face[] face_;

    public boolean transformed_;

    boolean gothead_;

    public Matrix3D mat_;

    public double xmin_, xmax_, ymin_, ymax_, zmin_, zmax_;

    double[] vert_;

    int nverts_, nfaces_, nedges_;

    int[] vx_, vy_, tvert_, findex_;

    Color[] gr_;

    static final int MAX_VERTS = 100;

    OOGLOFF() {
        mat_ = new Matrix3D();
        vx_ = new int[MAX_VERTS];
        vy_ = new int[MAX_VERTS];
        nverts_ = 0;
        nedges_ = 0;
        nfaces_ = 0;
        vert_ = null;
        gr_ = null;
        mat_.xrot(0);
        mat_.yrot(0);
    }

    OOGLOFF(final URL _loc) {
        this();
        try {
            readObject(new InputStreamReader(_loc.openStream()));
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public OOGLOFF(final Reader _is) {
        this();
        try {
            readObject(_is);
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void readObject(final Reader _is) throws IOException {
        final StreamTokenizer stream = new StreamTokenizer(_is);
        stream.eolIsSignificant(true);
        stream.commentChar('#');
        gothead_ = false;
        scanhead: while (!gothead_) {
            switch(stream.nextToken()) {
                default:
                    break scanhead;
                case StreamTokenizer.TT_EOL:
                    break;
                case StreamTokenizer.TT_WORD:
                    if ("OFF".equals(stream.sval)) {
                        nverts_ = 0;
                        nfaces_ = 0;
                        nedges_ = 0;
                        while (stream.nextToken() == StreamTokenizer.TT_EOL) {
                        }
                        ;
                        if (stream.ttype == StreamTokenizer.TT_NUMBER) {
                            nverts_ = (int) stream.nval;
                            if (stream.nextToken() == StreamTokenizer.TT_NUMBER) {
                                nfaces_ = (int) stream.nval;
                                if (stream.nextToken() == StreamTokenizer.TT_NUMBER) {
                                    nedges_ = (int) stream.nval;
                                    gothead_ = true;
                                } else {
                                    throw new IOException("Can't read OFF file");
                                }
                            } else {
                                throw new IOException("Can't read OFF file");
                            }
                        } else {
                            throw new IOException("Can't read OFF file");
                        }
                    }
                    break;
                case StreamTokenizer.TT_NUMBER:
                    break;
            }
        }
        endStream(stream);
    }

    private void endStream(final StreamTokenizer _stream) throws IOException {
        vert_ = new double[nverts_ * 3];
        face_ = new Face[nfaces_];
        findex_ = new int[nfaces_];
        for (int i = 0; i < nfaces_; i++) {
            findex_[i] = i;
        }
        int num = 0;
        int coordnum = 0;
        while (num < nverts_) {
            switch(_stream.nextToken()) {
                default:
                    break;
                case StreamTokenizer.TT_EOL:
                    if (coordnum > 2) {
                        coordnum = 0;
                        num++;
                    }
                    break;
                case StreamTokenizer.TT_NUMBER:
                    if (coordnum < 3) {
                        vert_[num * 3 + coordnum] = _stream.nval;
                        coordnum++;
                    }
            }
        }
        num = 0;
        coordnum = 0;
        boolean gotnum = false;
        while (num < nfaces_) {
            switch(_stream.nextToken()) {
                default:
                    break;
                case StreamTokenizer.TT_EOL:
                    if (gotnum) {
                        num++;
                    }
                    gotnum = false;
                    break;
                case StreamTokenizer.TT_NUMBER:
                    if (!gotnum) {
                        face_[num] = new Face();
                        face_[num].numVerts((int) _stream.nval);
                        gotnum = true;
                        coordnum = 0;
                    } else if (coordnum < face_[num].nverts_) {
                        face_[num].index_[coordnum] = 3 * (int) _stream.nval;
                        coordnum++;
                    } else {
                        face_[num].cr_ = 255;
                        face_[num].cg_ = 255;
                        face_[num].cb_ = 255;
                        double val = _stream.nval;
                        if (val <= 1) {
                            val *= 255;
                        }
                        face_[num].cr_ = (int) val;
                        if (_stream.nextToken() != StreamTokenizer.TT_EOL) {
                            val = _stream.nval;
                            if (val <= 1) {
                                val *= 255;
                            }
                            face_[num].cg_ = (int) val;
                            if (_stream.nextToken() != StreamTokenizer.TT_EOL) {
                                val = _stream.nval;
                                if (val <= 1) {
                                    val *= 255;
                                }
                                face_[num].cb_ = (int) val;
                            } else {
                                face_[num].cr_ = 255;
                                face_[num].cg_ = 255;
                                face_[num].cb_ = 255;
                                num++;
                                gotnum = false;
                            }
                        } else {
                            face_[num].cr_ = 255;
                            face_[num].cg_ = 255;
                            face_[num].cb_ = 255;
                            num++;
                            gotnum = false;
                        }
                    }
                    break;
            }
        }
    }

    void transform() {
        if (transformed_ || nverts_ <= 0) {
            return;
        }
        if (tvert_ == null) {
            tvert_ = new int[nverts_ * 3];
        }
        mat_.transform(vert_, tvert_, nverts_);
        transformed_ = true;
    }

    /**
   * The quick sort algorithm in this method is used for sorting faces from back to front by z depth values.
   */
    void qs(final int _left, final int _right) {
        int i, j, x, y;
        i = _left;
        j = _right;
        x = face_[findex_[(_left + _right) / 2]].zdepth_;
        do {
            while (face_[findex_[i]].zdepth_ > x && i < _right) {
                i++;
            }
            while (x > face_[findex_[j]].zdepth_ && j > _left) {
                j--;
            }
            if (i <= j) {
                y = findex_[i];
                findex_[i] = findex_[j];
                findex_[j] = y;
                i++;
                j--;
            }
        } while (i <= j);
        if (_left < j) {
            qs(_left, j);
        }
        if (i < _right) {
            qs(i, _right);
        }
    }

    public void paint(final Graphics _g) {
        if (vert_ == null || nverts_ <= 0) {
            return;
        }
        transform();
        if (gr_ == null) {
            gr_ = new Color[nfaces_];
            for (int i = 0; i < nfaces_; i++) {
                gr_[i] = new Color(face_[i].cr_, face_[i].cg_, face_[i].cb_);
            }
        }
        for (int i = 0; i < nfaces_; i++) {
            face_[i].zdepth_ = 0;
            for (int c = 0; c < face_[i].nverts_; c++) {
                if (face_[i].zdepth_ < tvert_[face_[i].index_[c] + 2]) {
                    face_[i].zdepth_ = tvert_[face_[i].index_[c] + 2];
                }
            }
        }
        qs(0, nfaces_ - 1);
        for (int f = 0; f < nfaces_; f++) {
            final int i = findex_[f];
            int v = 0;
            for (int c = 0; c < face_[i].nverts_; c++) {
                vx_[c] = tvert_[face_[i].index_[c]];
                vy_[c] = tvert_[face_[i].index_[c] + 1];
            }
            _g.setColor(gr_[i]);
            _g.fillPolygon(vx_, vy_, face_[i].nverts_);
            _g.setColor(gr_[i].darker());
            for (v = 0; v < (face_[i].nverts_ - 1); v++) {
                _g.drawLine(vx_[v], vy_[v], vx_[v + 1], vy_[v + 1]);
            }
            _g.drawLine(vx_[v], vy_[v], vx_[0], vy_[0]);
        }
    }

    public void findBB() {
        if (nverts_ <= 0) {
            return;
        }
        final double[] v = vert_;
        double xminTmp = v[0], xmaxTmp = xminTmp;
        double yminTmp = v[1], ymaxTmp = yminTmp;
        double zminTmp = v[2], zmaxTmp = zminTmp;
        for (int i = nverts_ * 3; i > 0; i -= 3) {
            final double x = v[i];
            if (x < xminTmp) {
                xminTmp = x;
            }
            if (x > xmaxTmp) {
                xmaxTmp = x;
            }
            final double y = v[i + 1];
            if (y < yminTmp) {
                yminTmp = y;
            }
            if (y > ymaxTmp) {
                ymaxTmp = y;
            }
            final double z = v[i + 2];
            if (z < zminTmp) {
                zminTmp = z;
            }
            if (z > zmaxTmp) {
                zmaxTmp = z;
            }
        }
        this.xmax_ = xmaxTmp;
        this.xmin_ = xminTmp;
        this.ymax_ = ymaxTmp;
        this.ymin_ = yminTmp;
        this.zmax_ = zmaxTmp;
        this.zmin_ = zminTmp;
    }
}
