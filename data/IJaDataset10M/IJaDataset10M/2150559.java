/*
	Naked Objects - a framework that exposes behaviourally complete
	business objects directly to the user.
	Copyright (C) 2000 - 2003  Naked Objects Group Ltd

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

	The authors can be contacted via www.nakedobjects.org (the
	registered address of Naked Objects Group is Kingsway House, 123 Goldworth
	Road, Woking GU21 1NR, UK).
*/

package org.nakedobjects.application.office.view;

import org.nakedobjects.application.office.Address;
import org.nakedobjects.application.office.Message;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.collection.List<>;
import org.nakedobjects.object.reflect.Field;
import org.nakedobjects.viewer.lightweight.AbstractCompositeView;
import org.nakedobjects.viewer.lightweight.Canvas;
import org.nakedobjects.viewer.lightweight.DragSource;
import org.nakedobjects.viewer.lightweight.DragTarget;
import org.nakedobjects.viewer.lightweight.InternalView;
import org.nakedobjects.viewer.lightweight.Padding;
import org.nakedobjects.viewer.lightweight.RootView;
import org.nakedobjects.viewer.lightweight.Style;
import org.nakedobjects.viewer.lightweight.View;
import org.nakedobjects.viewer.lightweight.Style.Text;
import org.nakedobjects.viewer.lightweight.util.StackLayout;
import org.nakedobjects.viewer.lightweight.util.ViewFactory;
import org.nakedobjects.viewer.lightweight.view.RootBorder;


public abstract class MessageView extends AbstractCompositeView implements RootView, DragSource, DragTarget {
	private static Contents contentsPrototype = new Contents();
	private static Addresses addressesPrototype = new Addresses();

    public MessageView() {
        setLayout(new StackLayout(true));
        setBorder(new RootBorder());
    }

    public Padding getPadding() {
    	Padding padding = super.getPadding();
    	
        // labels
        Field[] fields = getObject().getNakedClass().getFields();
        int width = 0;

        for (int i = 0; i < fields.length; i++) {
            int labelWidth = Style.LABEL.stringWidth(fields[i].getFirstName()) + 10;
            width = Math.max(width, labelWidth);
        }

        padding.extendLeft(width);
        
        return padding;
    }
    
    protected Text getTitleTextStyle() {
		return Style.TITLE;
	}

    private void addViews(Message object) {
		NakedClass cls = object.getNakedClass();

		String[] fields = fields();

		for (int f = 0; f < fields.length; f++) {
			Field field = cls.getField(fields[f]);
			Naked fieldObject = field.get(object);

			if (fieldObject instanceof List<>) {
				Class type = ((List<>) fieldObject).getType();
				if(type == Address.class) {
					addView((InternalView) addressesPrototype.makeView(field.get(object), field));
				} else if(type == NakedObject.class) {
					addView((InternalView) contentsPrototype.makeView(field.get(object), field));
				}
			}else {
				addView(ViewFactory.getViewFactory().createInternalView(fieldObject, field, true));
			}
            
		}
        
	}

	public void draw(Canvas g) {
        super.draw(g);

        int left = super.getPadding().getLeft();

        // labels
        View[] components = getComponents();

		Message message = (Message) getObject();
       	NakedClass cls = message.getNakedClass();

        String[] fields = fields();

		int top = getPadding().getTop();
        for (int f = 0; f < fields.length; f++) {
            Field field = cls.getField(fields[f]);
            int baseline = top + components[f].getBaseline();
            g.drawText(field.getFirstName() + ":", left + HPADDING, baseline, Style.IN_FOREGROUND, Style.LABEL);
            top += components[f].getBounds().getHeight();
        }
    }

    public String toString() {
        return "Large Message";
    }

    protected abstract String[] fields();

    protected void init(NakedObject object) {
        super.init(object);
		addViews((Message) object);
        object.resolve();
    }
}
