package com.google.gwt.gwtai.demo.client;

import com.google.gwt.gwtai.applet.client.Applet;
import com.google.gwt.gwtai.applet.client.Archive;
import com.google.gwt.gwtai.applet.client.Height;
import com.google.gwt.gwtai.applet.client.ImplementingClass;
import com.google.gwt.gwtai.applet.client.Width;

@ImplementingClass(com.google.gwt.gwtai.demo.impl.CallbackAppletImpl.class)
@Height("100")
@Width("350")
@Archive("GwtAI-Core.jar,com.google.gwt.gwtai.demo.GwtAI/GwtAI-Demo.jar")
public interface CallbackApplet extends Applet {
}
