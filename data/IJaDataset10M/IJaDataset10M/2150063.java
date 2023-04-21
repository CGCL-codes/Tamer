package com.aula.carrinho;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.SurfaceHolder;
import com.geomanalitica.utils.Vetorizador;
import com.geomanalitica.utils._2d.Ponto2D;
import com.geomanalitica.utils._2d.Vetor2D;
import java.util.ArrayList;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Sample4View extends SampleViewBase {

    public static final int X_INICIAL = 160;

    public static final int Y_INICIAL = 1;

    private Mat mYuv;

    private Mat mRgba;

    private Mat mGraySubmat;

    private Mat mIntermediateMat;

    private final TelaActivity tela;

    public Sample4View(Context context) {
        super(context);
        this.tela = (TelaActivity) context;
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        super.surfaceChanged(_holder, format, width, height);
        synchronized (this) {
            mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);
            mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());
            mRgba = new Mat();
            mIntermediateMat = new Mat();
        }
    }

    byte contaPrint = 0;

    @Override
    protected Bitmap processFrame(byte[] data) {
        mYuv.put(0, 0, data);
        Imgproc.Canny(mGraySubmat, mIntermediateMat, 80, 100);
        Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2BGR, 4);
        Bitmap bmp = Bitmap.createBitmap(getFrameWidth(), getFrameHeight(), Bitmap.Config.ARGB_8888);
        System.out.println("Print");
        contaPrint = 0;
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                bmp.setPixel(i, j, Color.BLACK);
            }
        }
        ArrayList<Ponto2D> pontos;
        pontos = Vetorizador.vetorizar(mRgba);
        if (pontos != null && pontos.size() > 1) {
        } else {
            return bmp;
        }
        Vetor2D centro = new Vetor2D(new Ponto2D(mRgba.rows() / 2, 0), new Ponto2D(mRgba.rows() / 2, mRgba.cols()));
        ArrayList<Ponto2D> otm = new ArrayList<Ponto2D>(3);
        otm.add(pontos.get(0));
        if (pontos.size() > 2) {
            otm.add(pontos.get(pontos.size() / 2 + 1));
        }
        otm.add(pontos.get(pontos.size() - 1));
        Vetor2D vetor2D;
        if (otm.size() == 3) {
            vetor2D = new Vetor2D(otm.get(1), otm.get(2));
        } else {
            vetor2D = new Vetor2D(otm.get(0), otm.get(1));
        }
        final float anguloInterno = Vetor2D.anguloInterno(centro, vetor2D);
        System.out.println("Angulo: " + anguloInterno);
        int potenciaEsquerda = 99;
        int potenciaDireita = 99;
        float dif = anguloInterno - 90;
        if (dif > 0) {
            potenciaDireita -= dif * 3;
        } else {
            potenciaEsquerda += dif * 3;
        }
        tela.enviarPotencia(potenciaEsquerda, potenciaDireita);
        for (int i = 0; otm != null && i < otm.size() - 1; i++) {
            Ponto2D ponto = otm.get(i);
            Ponto2D proxPonto = otm.get(i + 1);
            linhaToBmp((int) ponto.getX(), (int) ponto.getY(), (int) proxPonto.getX(), (int) proxPonto.getY(), bmp);
        }
        return bmp;
    }

    private byte sinal(int x) {
        return (byte) ((x < 0) ? -1 : ((x > 0) ? 1 : 0));
    }

    private void linhaToBmp(int x1, int y1, int x2, int y2, Bitmap bmp) {
        int dx, dy, sdx, sdy, px, py, dxabs, dyabs, i;
        float inclinacao;
        dx = x2 - x1;
        dy = y2 - y1;
        dxabs = Math.abs(dx);
        dyabs = Math.abs(dy);
        sdx = sinal(dx);
        sdy = sinal(dy);
        if (dxabs >= dyabs) {
            inclinacao = (float) dy / (float) dx;
            for (i = 0; i != dx; i += sdx) {
                px = i + x1;
                py = (int) (inclinacao * i + y1);
                bmp.setPixel(px, py, Color.RED);
            }
        } else {
            inclinacao = (float) dx / (float) dy;
            for (i = 0; i != dy; i += sdy) {
                px = (int) (inclinacao * i + x1);
                py = i + y1;
                bmp.setPixel(px, py, Color.RED);
            }
        }
    }

    private boolean[][] converterParaPretoBranco(Mat mat) {
        boolean[][] pretoBranco = new boolean[mat.rows()][mat.cols()];
        final int colunas = mRgba.cols() / 2;
        for (int r = 0; r < mRgba.rows(); r++) {
            for (int c = 0; c < colunas; c++) {
                pretoBranco[r][c] = mRgba.get(r, c)[0] > 0;
            }
        }
        return pretoBranco;
    }

    @Override
    public void run() {
        super.run();
        synchronized (this) {
            if (mYuv != null) {
                mYuv.release();
            }
            if (mRgba != null) {
                mRgba.release();
            }
            if (mGraySubmat != null) {
                mGraySubmat.release();
            }
            if (mIntermediateMat != null) {
                mIntermediateMat.release();
            }
            mYuv = null;
            mRgba = null;
            mGraySubmat = null;
            mIntermediateMat = null;
        }
    }

    public native void FindFeatures(long matAddrGr, long matAddrRgba);

    static {
        System.loadLibrary("mixed_sample");
    }
}
