package mw.server.model.effect;

import groovy.lang.Closure;
import java.util.ArrayList;
import java.util.List;
import mw.server.model.Card;
import mw.utils.ClosureUtil;

public class AddAspect extends ContiniousEffect {

    private static final long serialVersionUID = 1L;

    private Card target;

    private Closure listDynamic;

    private Closure aspectDynamic;

    private String aspect;

    private Object value;

    private List<AddAspect> effects = new ArrayList<AddAspect>();

    private List<String> aspects = new ArrayList<String>();

    public AddAspect(Card source, Card target, String aspect) {
        super(source);
        this.target = target;
        this.aspect = aspect;
    }

    public AddAspect(Card source, Card target, String aspect, Object value) {
        super(source);
        this.target = target;
        this.aspect = aspect;
        this.value = value;
    }

    public AddAspect(Card source, Card target, Closure aspect, Object value) {
        super(source);
        this.target = target;
        this.aspectDynamic = aspect;
        this.value = value;
    }

    public AddAspect(Card source, Closure list, String aspect) {
        super(source);
        this.listDynamic = list;
        this.aspect = aspect;
    }

    public AddAspect(Card source, Closure list, Closure aspect, Object value) {
        super(source);
        this.listDynamic = list;
        this.aspectDynamic = aspect;
        this.value = value;
    }

    @Override
    public void applyEffect() {
        if (listDynamic != null) {
            for (Card card : ClosureUtil.getList(source, listDynamic)) {
                if (aspectDynamic != null) {
                    for (String __aspect : ClosureUtil.getListString(source, aspectDynamic)) {
                        AddAspect addAspect = new AddAspect(source, card, __aspect, value);
                        addAspect.applyEffect();
                        effects.add(addAspect);
                        aspects.add(__aspect);
                    }
                } else {
                    AddAspect addAspect = new AddAspect(source, card, aspect, value);
                    addAspect.applyEffect();
                    effects.add(addAspect);
                }
            }
        } else {
            if (aspectDynamic != null) {
                for (String __aspect : ClosureUtil.getListString(source, aspectDynamic)) {
                    if (value != null) {
                        target.addAspect(__aspect, value);
                    } else {
                        target.addAspect(__aspect, 0);
                    }
                    aspects.add(__aspect);
                }
            } else {
                if (value != null) {
                    target.addAspect(aspect, value);
                } else {
                    target.addAspect(aspect, 0);
                }
            }
        }
    }

    @Override
    public void discardEffect() {
        if (listDynamic != null) {
            for (AddAspect addAspect : effects) {
                addAspect.discardEffect();
            }
        } else {
            if (aspectDynamic != null) {
                for (String __aspect : this.aspects) {
                    target.removeAspect(__aspect);
                }
            } else {
                target.removeAspect(aspect);
            }
        }
    }

    @Override
    public Layer getLayer() {
        return Layer.ABILITY;
    }
}
