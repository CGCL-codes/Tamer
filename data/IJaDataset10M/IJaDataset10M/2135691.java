package com.nzep.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import com.nzep.device.Compass;
import com.nzep.device.SensorAccessManager;

/**
 * Repr�sentation d'une boussole
 * Indique l'orientation du Nord, pivote de 0 � 359�
 * Animation douce automatique
 * Support des deux modes d'orientation utilisateurs
 * 
 * @author Sebastien Villemain
 *
 */
public class CompassView extends View {

    /**
     * Delais entre chaque image
     */
    private static final int DELAY = 20;

    /**
	 * Dur�e de l'animation
	 */
    private static final int DURATION = 1000;

    /**
	 * Nord de la boussole
	 */
    private float northOrientation = 0;

    /**
	 * Paint du cercle de la boussole
	 */
    private Paint circlePaint = null;

    /**
	 * Paint du nord
	 */
    private Paint northPaint = null;

    /**
	 * Paint du sud
	 */
    private Paint southPaint = null;

    /**
	 * Contour du triangle
	 */
    private Path trianglePath = null;

    /**
	 * D�fini la position de d�but de l'animation
	 */
    private float startNorthOrientation = 0;

    /**
	 * D�fini la position de de fin de l'animation
	 */
    private float endNorthOrientation = 0;

    /**
	 * Heure de d�but de l�animation (ms)
	 */
    private long startTime;

    /**
     * Constructeur par d�faut de la vue
     * 
     * @param context
     */
    public CompassView(Context context) {
        super(context);
        initialise();
    }

    /**
	 * Constructeur utilis� pour instancier la vue depuis sa
	 * d�claration dans un fichier XML
	 * 
	 * @param context
	 * @param attrs
	 */
    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    /**
	 * Constructeur utilis� pour instancier la vue depuis sa
	 * d�claration dans un fichier XML
	 * 
	 * @param context
	 * @param attrs
	 */
    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise();
    }

    /**
     * Initialise la vue
     */
    private void initialise() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.TRANSPARENT);
        northPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        northPaint.setColor(Color.RED);
        southPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        southPaint.setColor(Color.BLUE);
        trianglePath = new Path();
        Compass compass = SensorAccessManager.getCompass();
        if (compass != null) {
            compass.addCompassViewListener(this);
            compass.setNorthOrientation((int) northOrientation);
        }
    }

    /**
	 * Suppression de la vue
	 */
    public void dispose() {
        Compass compass = SensorAccessManager.getCompass();
        if (compass != null) {
            compass.removeCompassViewListener(this);
        }
        destroyDrawingCache();
    }

    /**
	 * D�terminer la taille de notre vue
	 * 
	 * @param measureSpec
	 * @return
	 */
    private static int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }

    /**
	 * Permet de changer l'orientation de la boussole
	 * 
	 * @param rotation
	 */
    public void setNorthOrientation(float rotation) {
        if (rotation != northOrientation) {
            removeCallbacks(animate);
            startNorthOrientation = northOrientation;
            endNorthOrientation = rotation;
            if (((startNorthOrientation + 180) % 360) > endNorthOrientation) {
                if ((startNorthOrientation - endNorthOrientation) > 180) {
                    endNorthOrientation += 360;
                }
            } else {
                if ((endNorthOrientation - startNorthOrientation) > 180) {
                    startNorthOrientation += 360;
                }
            }
            startTime = SystemClock.uptimeMillis();
            postDelayed(animate, DELAY);
        }
    }

    /**
	 * Permet de d�finir la taille de notre vue
	 * par d�faut un cadre de 100x100 si non red�fini
	 */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(d, d);
    }

    /**
	 * Appel�e pour redessiner la vue
	 */
    protected void onDraw(Canvas canvas) {
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        int radius = Math.min(centerX, centerY);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        canvas.save();
        canvas.rotate(-northOrientation, centerX, centerY);
        trianglePath.reset();
        trianglePath.moveTo(centerX, 10);
        trianglePath.lineTo(centerX - 10, centerY);
        trianglePath.lineTo(centerX + 10, centerY);
        canvas.drawPath(trianglePath, northPaint);
        canvas.rotate(180, centerX, centerY);
        canvas.drawPath(trianglePath, southPaint);
        canvas.restore();
    }

    /**
	 * Calcul de l'animation
	 */
    private final Runnable animate = new Runnable() {

        /**
		 * Temps total depuis le d�but de l'animation
		 */
        private long totalTime = 0;

        /**
		 * Pourcentage d'�volution de l'animation
		 */
        private float perCent = 0;

        public void run() {
            totalTime = SystemClock.uptimeMillis() - startTime;
            if (totalTime > DURATION) {
                if (animate != null) removeCallbacks(animate);
                northOrientation = endNorthOrientation % 360;
            } else {
                perCent = ((float) totalTime) / DURATION;
                perCent = (float) Math.sin(perCent * 1.5);
                perCent = Math.min(perCent, 1);
                northOrientation = (float) (startNorthOrientation + perCent * (endNorthOrientation - startNorthOrientation));
                postDelayed(this, DELAY);
            }
            invalidate();
        }
    };
}
