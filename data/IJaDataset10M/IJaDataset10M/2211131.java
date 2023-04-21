package org.axcommunity.niagara.views;

import javax.baja.converters.BINumericToNumber;
import javax.baja.converters.BObjectToString;
import javax.baja.gx.BSize;
import javax.baja.naming.BOrd;
import javax.baja.naming.SlotPath;
import javax.baja.sys.BObject;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.BLabel;
import javax.baja.ui.BLayout;
import javax.baja.ui.BValueBinding;
import javax.baja.ui.enums.BHalign;
import javax.baja.ui.enums.BScaleMode;
import javax.baja.ui.enums.BValign;
import javax.baja.ui.pane.BCanvasPane;
import javax.baja.ui.pane.BGridPane;
import javax.baja.util.BFormat;
import javax.baja.workbench.view.*;
import org.axcommunity.niagara.system.BSysInfo;
import com.tridium.kitpx.BAnalogMeter;
import com.tridium.kitpx.BBargraph;
import com.tridium.kitpx.BBoundLabel;

/**
 * Did this as a learning exercise.  Wanted to learn how to create custom
 * BWbComponentView for an object.  This is meant as a rough template for
 * different ways you can do it.  What I have learned is how to properly overload
 * the doLoadValue and create widgets with bindings in it.  Also, learned how to create
 * agents in the module-inlcude.xml file.  This is a pretty rough view - I stink at 
 * graphics.  But, I'm hoping this inspires someone creative to build better object views. 
 *
 * @author    Mike Arnott, Kors Engineering
 */
public class BSysInfoView extends BWbComponentView {

    public BSysInfoView() {
    }

    public void doLoadValue(BObject obj, Context cx) {
        BSysInfo sysInfo = (BSysInfo) obj;
        SlotPath basePath = sysInfo.getSlotPath();
        BCanvasPane canvas = new BCanvasPane();
        canvas.setViewSize(BSize.make(235, 195));
        canvas.setScale(BScaleMode.fitRatio);
        BLabel stationName = new BLabel();
        stationName.setLayout(BLayout.makeAbs(0, 0, 235, 20));
        BValueBinding snameBind = new BValueBinding();
        snameBind.setPopupEnabled(false);
        SlotPath snamePath = basePath.merge(new SlotPath("stationName/value"));
        BOrd snameOrd = BOrd.make(sysInfo.getHandleOrd(), snamePath);
        snameBind.setOrd(snameOrd);
        BObjectToString snameCvt = new BObjectToString();
        snameCvt.setFormat(BFormat.make("Station Name: %.%"));
        snameBind.add("text", snameCvt);
        stationName.add(null, snameBind);
        canvas.add(null, stationName);
        BLabel niagaraVer = new BLabel();
        niagaraVer.setLayout(BLayout.makeAbs(0, 20, 235, 20));
        BValueBinding nverBind = new BValueBinding();
        nverBind.setPopupEnabled(false);
        SlotPath nverPath = basePath.merge(new SlotPath("niagaraVersion/value"));
        BOrd nverOrd = BOrd.make(sysInfo.getHandleOrd(), nverPath);
        nverBind.setOrd(nverOrd);
        BObjectToString nverCvt = new BObjectToString();
        nverCvt.setFormat(BFormat.make("Niagra Version: %.%"));
        nverBind.add("text", nverCvt);
        niagaraVer.add(null, nverBind);
        canvas.add(null, niagaraVer);
        BLabel osName = new BLabel();
        osName.setLayout(BLayout.makeAbs(0, 40, 175, 20));
        osName.setHalign(BHalign.left);
        BValueBinding osNameBind = new BValueBinding();
        osNameBind.setPopupEnabled(false);
        SlotPath osNamePath = basePath.merge(new SlotPath("osName/value"));
        BOrd osNameOrd = BOrd.make(sysInfo.getHandleOrd(), osNamePath);
        osNameBind.setOrd(osNameOrd);
        BObjectToString osNameCvt = new BObjectToString();
        osNameCvt.setFormat(BFormat.make("OS Name: %.%"));
        osNameBind.add("text", osNameCvt);
        osName.add(null, osNameBind);
        canvas.add(null, osName);
        BLabel osVersion = new BLabel();
        osVersion.setHalign(BHalign.right);
        osVersion.setLayout(BLayout.makeAbs(175, 40, 60, 20));
        BValueBinding osVersionBind = new BValueBinding();
        osVersionBind.setPopupEnabled(false);
        SlotPath osVersionPath = basePath.merge(new SlotPath("osVersion/value"));
        BOrd osVersionOrd = BOrd.make(sysInfo.getHandleOrd(), osVersionPath);
        osVersionBind.setOrd(osVersionOrd);
        BObjectToString osVersionCvt = new BObjectToString();
        osVersionCvt.setFormat(BFormat.make("Ver: %.%"));
        osVersionBind.add("text", osVersionCvt);
        osVersion.add(null, osVersionBind);
        canvas.add(null, osVersion);
        BAnalogMeter cpuMeter = new BAnalogMeter();
        cpuMeter.setLayout(BLayout.makeAbs(0, 80, 95, 115));
        cpuMeter.setNumDivisions(10);
        cpuMeter.setMax(100);
        cpuMeter.setText("Percent %");
        BValueBinding cpuBind = new BValueBinding();
        cpuBind.setPopupEnabled(false);
        SlotPath cpuBindPath = basePath.merge(new SlotPath("cpuUsage/value"));
        BOrd cpuBindOrd = BOrd.make(sysInfo.getHandleOrd(), cpuBindPath);
        cpuBind.setOrd(cpuBindOrd);
        BINumericToNumber cpuCvt = new BINumericToNumber();
        cpuBind.add("value", cpuCvt);
        cpuMeter.add(null, cpuBind);
        canvas.add(null, cpuMeter);
        BBargraph freeMem = new BBargraph();
        freeMem.setLayout(BLayout.makeAbs(100, 80, 60, 115));
        freeMem.setMax(sysInfo.getTotalPhysicalMemory().getValue());
        freeMem.setText("");
        freeMem.setScale(freeMem.getMax() / 8);
        BValueBinding freeMemBind = new BValueBinding();
        freeMemBind.setPopupEnabled(false);
        SlotPath freeMemBindPath = basePath.merge(new SlotPath("freePhysicalMemory/value"));
        BOrd freeMemBindOrd = BOrd.make(sysInfo.getHandleOrd(), freeMemBindPath);
        freeMemBind.setOrd(freeMemBindOrd);
        BINumericToNumber freeMemCvt = new BINumericToNumber();
        freeMemBind.add("value", freeMemCvt);
        freeMem.add(null, freeMemBind);
        canvas.add(null, freeMem);
        BBargraph freeHeap = new BBargraph();
        freeHeap.setLayout(BLayout.makeAbs(165, 80, 60, 115));
        freeHeap.setMax(sysInfo.getTotalHeap().getValue());
        freeHeap.setText("");
        freeHeap.setScale(freeHeap.getMax() / 8);
        BValueBinding freeHeapBind = new BValueBinding();
        freeHeapBind.setPopupEnabled(false);
        SlotPath freeHeapBindPath = basePath.merge(new SlotPath("freeHeap/value"));
        BOrd freeHeapBindOrd = BOrd.make(sysInfo.getHandleOrd(), freeHeapBindPath);
        freeHeapBind.setOrd(freeHeapBindOrd);
        BINumericToNumber freeHeapCvt = new BINumericToNumber();
        freeHeapBind.add("value", freeHeapCvt);
        freeHeap.add(null, freeHeapBind);
        BValueBinding totalHeapBind = new BValueBinding();
        totalHeapBind.setPopupEnabled(false);
        SlotPath totalHeapBindPath = basePath.merge(new SlotPath("totalHeap/value"));
        BOrd totalHeapBindOrd = BOrd.make(sysInfo.getHandleOrd(), totalHeapBindPath);
        totalHeapBind.setOrd(totalHeapBindOrd);
        BINumericToNumber totalHeapCvt = new BINumericToNumber();
        totalHeapBind.add("max", totalHeapCvt);
        freeHeap.add(null, totalHeapBind);
        canvas.add(null, freeHeap);
        BLabel cpuLabel = new BLabel();
        cpuLabel.setLayout(BLayout.makeAbs(0, 65, 95, 15));
        cpuLabel.setText("CPU Usage");
        canvas.add(null, cpuLabel);
        BLabel ramLabel = new BLabel();
        ramLabel.setLayout(BLayout.makeAbs(100, 65, 60, 15));
        ramLabel.setText("Free RAM");
        canvas.add(null, ramLabel);
        BLabel heapLabel = new BLabel();
        heapLabel.setLayout(BLayout.makeAbs(165, 65, 60, 15));
        heapLabel.setText("Free Heap");
        canvas.add(null, heapLabel);
        setContent(canvas);
    }

    public Type getType() {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BSysInfoView.class);
}
