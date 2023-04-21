package org.sf.spring3d.scene.view.projection.impl;

import javax.media.opengl.glu.GLU;
import org.sf.spring3d.scene.node.impl.DefaultSceneNode;
import org.sf.spring3d.scene.view.engine.impl.DrawContext;
import org.sf.spring3d.scene.view.projection.IPerspective;

/**
 * TODO_DOCUMENT_ME
 * 
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public class PerspectiveProjection<T extends Number> extends DefaultSceneNode<DrawContext> implements IPerspective<T> {

    private Class<T> _precision = (Class<T>) Float.class;

    private T _fieldOfView;

    private T _aspectRatio;

    private T _nearPlane;

    private T _farPlane;

    /**
    * TODO_DOCUMENT_ME
    * 
    * @since 0.0.1
    */
    public PerspectiveProjection() {
    }

    public PerspectiveProjection(Class<T> precision) {
        setPrecision(precision);
    }

    public PerspectiveProjection(T fieldOfView, T aspectRatio, T nearPlane, T farPlane, Class<T> precision) {
        setPrecision(precision);
        setFieldOfView(fieldOfView);
        setAspectRatio(aspectRatio);
        setNearPlane(nearPlane);
        setFarPlane(farPlane);
    }

    public static final PerspectiveProjection<Float> createPerspectiveProjection(Float fieldOfView, Float aspectRatio, Float nearPlane, Float farPlane) {
        return new PerspectiveProjection<Float>(fieldOfView, aspectRatio, nearPlane, farPlane, Float.class);
    }

    public static final PerspectiveProjection<Double> createPerspectiveProjection(Double fieldOfView, Double aspectRatio, Double nearPlane, Double farPlane) {
        return new PerspectiveProjection<Double>(fieldOfView, aspectRatio, nearPlane, farPlane, Double.class);
    }

    @Override
    public void draw(DrawContext drawContext) {
        super.draw(drawContext);
        int height = (drawContext.getHeight() <= 0) ? 1 : drawContext.getHeight();
        int width = drawContext.getWidth();
        float aspectRatio = (float) width / (float) height;
        if (Double.class.equals(getPrecision())) {
            new GLU().gluPerspective(getFieldOfView().doubleValue(), (getAspectRatio() == null) ? aspectRatio : getAspectRatio().doubleValue(), getNearPlane().doubleValue(), getFarPlane().doubleValue());
        } else {
            new GLU().gluPerspective(getFieldOfView().floatValue(), (getAspectRatio() == null) ? aspectRatio : getAspectRatio().floatValue(), getNearPlane().floatValue(), getFarPlane().floatValue());
        }
    }

    /**
    * @return the precision
    */
    public Class<T> getPrecision() {
        return _precision;
    }

    /**
    * @param precision the precision to set
    */
    public void setPrecision(Class<T> precision) {
        _precision = precision;
    }

    /**
    * @return the fieldOfView
    */
    public T getFieldOfView() {
        return _fieldOfView;
    }

    /**
    * @param fieldOfView the fieldOfView to set
    */
    public void setFieldOfView(T fieldOfView) {
        _fieldOfView = fieldOfView;
    }

    /**
    * @return the aspectRatio
    */
    public T getAspectRatio() {
        return _aspectRatio;
    }

    /**
    * @param aspectRatio the aspectRatio to set
    */
    public void setAspectRatio(T aspectRatio) {
        _aspectRatio = aspectRatio;
    }

    /**
    * @return the farPlane
    */
    public T getFarPlane() {
        return _farPlane;
    }

    /**
    * @param farPlane the farPlane to set
    */
    public void setFarPlane(T farPlane) {
        _farPlane = farPlane;
    }

    /**
    * @return the nearPlane
    */
    public T getNearPlane() {
        return _nearPlane;
    }

    /**
    * @param nearPlane the nearPlane to set
    */
    public void setNearPlane(T nearPlane) {
        _nearPlane = nearPlane;
    }
}
