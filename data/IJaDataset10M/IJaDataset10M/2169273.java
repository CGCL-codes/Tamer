package org.onemind.swingweb.client.gwt.widget;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class Slider extends SimplePanel implements HasOrientation, SourcesChangeEvents {

    private class TrackSlide extends SimplePanel implements SourcesChangeEvents, MouseListener {

        private int _trackSize = 4;

        private int _slideSize = 10;

        private int _slideLength = _slideSize / 2;

        private int _minValue = 0;

        private int _maxValue = 100;

        private int _value = 0;

        private int _tempValue;

        private boolean _capture;

        private int _startX, _startY;

        private ChangeListenerCollection _changeListenerCol = new ChangeListenerCollection();

        private SimplePanel _track = new SimplePanel();

        private MousePanel _slide = new MousePanel();

        private Label _slideTip = new Label();

        private TrackSlide() {
            super();
            DOM.setAttribute(_track.getElement(), "className", "slider-track");
            setWidget(_track);
            _track.setWidget(_slide);
            DOM.setAttribute(_slide.getElement(), "className", "slider-slide");
            DOM.setStyleAttribute(_slide.getElement(), "zIndex", "1");
            DOM.setStyleAttribute(_slide.getElement(), "position", "relative");
            _slide.addMouseListener(this);
            _slide.setWidget(_slideTip);
            DOM.setStyleAttribute(_slide.getElement(), "float", "left");
        }

        public void onMouseDown(Widget sender, int x, int y) {
            _capture = true;
            _startX = x;
            _startY = y;
            _tempValue = _value;
            DOM.setCapture(_slide.getElement());
        }

        public void onMouseEnter(Widget sender) {
        }

        public void onMouseLeave(Widget sender) {
        }

        public void onMouseMove(Widget sender, int x, int y) {
            if (_capture) {
                if (_orientation == HORIZONTAL) {
                    int absX = _slide.getAbsoluteLeft() + x;
                    int newX = absX - _startX;
                    int maxX = _width - _slideLength;
                    System.out.println("move:MaxX = " + maxX);
                    int relativeX = newX - _track.getAbsoluteLeft();
                    if (relativeX < 0) {
                        relativeX = 0;
                    } else if (relativeX > maxX) {
                        relativeX = maxX;
                    }
                    _tempValue = computeValue(maxX, relativeX);
                    checkSlidePos(_tempValue);
                } else {
                    int absY = _slide.getAbsoluteTop() + y;
                    int newY = absY - _startY;
                    int maxY = _height - _slideLength;
                    int relativeY = newY - _track.getAbsoluteTop();
                    if (relativeY < 0) {
                        relativeY = 0;
                    } else if (relativeY > maxY) {
                        relativeY = maxY;
                    }
                    _tempValue = computeValue(maxY, relativeY);
                    checkSlidePos(_tempValue);
                }
            }
        }

        public void onMouseUp(Widget sender, int x, int y) {
            _capture = false;
            _slideTip.setText("");
            DOM.releaseCapture(_slide.getElement());
            if (_tempValue != _value) {
                _value = _tempValue;
                _changeListenerCol.fireChange(Slider.this);
                System.out.println("Fire changes");
            }
        }

        private int computeValue(int physicalRange, int physicalPos) {
            float percent = physicalPos / (float) physicalRange;
            int range = _maxValue - _minValue;
            int newValue = _minValue + (int) (range * percent);
            return newValue;
        }

        /**
         * Set the maxValue
         * @param maxValue The maxValue to set.
         */
        public final void setMaxValue(int maxValue) {
            if (maxValue != _maxValue) {
                _maxValue = maxValue;
                checkSlidePos();
            }
        }

        /**
         * Set the minValue
         * @param minValue The minValue to set.
         */
        public final void setMinValue(int minValue) {
            if (minValue != _minValue) {
                _minValue = minValue;
                checkSlidePos();
            }
        }

        /**
         * Return the maxValue
         * @return the maxValue.
         */
        public final int getMaxValue() {
            return _maxValue;
        }

        /**
         * Return the minValue
         * @return the minValue.
         */
        public final int getMinValue() {
            return _minValue;
        }

        /**
         * Return the value
         * @return the value.
         */
        public final int getValue() {
            return _value;
        }

        /**
         * Set the value
         * @param value The value to set.
         */
        public final void setValue(int value) {
            if (_value != value) {
                _value = value;
                checkSlidePos();
            }
        }

        public void init(OrientationConstant orientation, int width, int height) {
            int slideOffset = (_trackSize - _slideSize) / 2;
            if (orientation == VERTICAL) {
                int margin = (width - _trackSize) / 2;
                DOM.setStyleAttribute(_track.getElement(), "float", "left");
                DOM.setStyleAttribute(_track.getElement(), "width", String.valueOf(_trackSize));
                DOM.setStyleAttribute(_track.getElement(), "height", String.valueOf(height));
                DOM.setStyleAttribute(_track.getElement(), "marginLeft", String.valueOf(margin));
                DOM.setStyleAttribute(_track.getElement(), "marginTop", "0");
                DOM.setStyleAttribute(_slide.getElement(), "width", String.valueOf(_slideSize));
                DOM.setStyleAttribute(_slide.getElement(), "height", String.valueOf(_slideLength));
                DOM.setStyleAttribute(_slide.getElement(), "left", String.valueOf(slideOffset));
            } else {
                int margin = (height - _trackSize) / 2;
                DOM.setStyleAttribute(_track.getElement(), "width", String.valueOf(width));
                DOM.setStyleAttribute(_track.getElement(), "height", String.valueOf(_trackSize));
                DOM.setStyleAttribute(_track.getElement(), "marginTop", String.valueOf(margin));
                DOM.setStyleAttribute(_track.getElement(), "marginLeft", "0");
                DOM.setStyleAttribute(_slide.getElement(), "height", String.valueOf(_slideSize));
                DOM.setStyleAttribute(_slide.getElement(), "width", String.valueOf(_slideLength));
                DOM.setStyleAttribute(_slide.getElement(), "top", String.valueOf(slideOffset));
            }
            checkSlidePos();
        }

        private void checkSlidePos() {
            checkSlidePos(_value);
        }

        private void checkSlidePos(int value) {
            if (_orientation == HORIZONTAL) {
                if (_width > 0) {
                    int fullRange = _maxValue - _minValue;
                    float valuePercent = (float) (value - _minValue) / fullRange;
                    int maxX = _width - _slideLength;
                    int left = (int) (valuePercent * maxX);
                    DOM.setStyleAttribute(_slide.getElement(), "top", String.valueOf(-((_slideSize - _trackSize) / 2)));
                    DOM.setStyleAttribute(_slide.getElement(), "left", String.valueOf(left));
                }
            } else {
                if (_height > 0) {
                    int fullRange = _maxValue - _minValue;
                    float valuePercent = (float) (value - _minValue) / fullRange;
                    int maxY = _height - _slideLength;
                    int top = (int) (valuePercent * maxY);
                    DOM.setStyleAttribute(_slide.getElement(), "left", String.valueOf(-((_slideSize - _trackSize) / 2)));
                    DOM.setStyleAttribute(_slide.getElement(), "top", String.valueOf(top));
                }
            }
        }

        public void addChangeListener(ChangeListener listener) {
            _changeListenerCol.add(listener);
        }

        public void removeChangeListener(ChangeListener listener) {
            _changeListenerCol.remove(listener);
        }
    }

    private OrientationConstant _orientation;

    private SimplePanel _tickArea = new SimplePanel();

    private int _width, _height;

    private FlexTable _flexTable;

    private TrackSlide _trackSlide = new TrackSlide();

    public Slider() {
        this(VERTICAL);
        initSlider();
    }

    private void initSlider() {
        _flexTable = new FlexTable();
        _flexTable.setWidth("100%");
        _flexTable.setHeight("100%");
        setWidget(_flexTable);
        if (_orientation == VERTICAL) {
            _flexTable.setWidget(0, 0, _trackSlide);
            _flexTable.setWidget(0, 1, _tickArea);
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(0, 0), "align", "center");
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(0, 1), "align", "center");
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(0, 0), "width", "50%");
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(0, 1), "width", "50%");
        } else {
            _flexTable.setWidget(0, 0, _trackSlide);
            _flexTable.setWidget(1, 0, _tickArea);
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(0, 0), "verticalAlign", "middle");
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(1, 0), "verticalAlign", "middle");
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(0, 0), "height", "50%");
            DOM.setStyleAttribute(_flexTable.getCellFormatter().getElement(1, 0), "height", "50%");
        }
        _trackSlide.init(_orientation, _width, _height);
    }

    public Slider(OrientationConstant orientation) {
        setOrientation(orientation);
    }

    public OrientationConstant getOrientation() {
        return _orientation;
    }

    public void setOrientation(OrientationConstant orientation) {
        if (_orientation != orientation) {
            _orientation = orientation;
            initSlider();
        }
    }

    /**
     * Set the maxValue
     * @param maxValue The maxValue to set.
     */
    public final void setMaxValue(int maxValue) {
        _trackSlide.setMaxValue(maxValue);
    }

    /**
     * Set the minValue
     * @param minValue The minValue to set.
     */
    public final void setMinValue(int minValue) {
        _trackSlide.setMinValue(minValue);
    }

    /**
     * Return the maxValue
     * @return the maxValue.
     */
    public final int getMaxValue() {
        return _trackSlide.getMaxValue();
    }

    /**
     * Return the minValue
     * @return the minValue.
     */
    public final int getMinValue() {
        return _trackSlide.getMinValue();
    }

    /**
     * Return the value
     * @return the value.
     */
    public final int getValue() {
        return _trackSlide.getValue();
    }

    /**
     * Set the value
     * @param value The value to set.
     */
    public final void setValue(int value) {
        _trackSlide.setValue(value);
    }

    /**
     * Set the width
     * @param width The width to set.
     */
    public final void setWidth(String width) {
        _width = Integer.parseInt(width);
        _trackSlide.init(_orientation, _width, _height);
        super.setWidth(width);
    }

    /**
     * Set the height
     * @param height The height to set.
     */
    public final void setHeight(String height) {
        _height = Integer.parseInt(height);
        _trackSlide.init(_orientation, _width, _height);
        super.setHeight(height);
    }

    public void addChangeListener(ChangeListener listener) {
        _trackSlide.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
    }
}
