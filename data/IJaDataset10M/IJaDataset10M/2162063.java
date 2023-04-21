package cn.edu.wuse.musicxml.parser;

import cn.edu.wuse.musicxml.print.Layout;
import cn.edu.wuse.musicxml.symbol.BeamPointEvent;
import cn.edu.wuse.musicxml.symbol.ClefSymbolEvent;
import cn.edu.wuse.musicxml.symbol.EndMeasureEvent;
import cn.edu.wuse.musicxml.symbol.EndPageEvent;
import cn.edu.wuse.musicxml.symbol.EndSystemEvent;
import cn.edu.wuse.musicxml.symbol.FontChangeEvent;
import cn.edu.wuse.musicxml.symbol.KeySymbolEvent;
import cn.edu.wuse.musicxml.symbol.MeasureNumberingChangeEvent;
import cn.edu.wuse.musicxml.symbol.MeasureSymbolEvent;
import cn.edu.wuse.musicxml.symbol.NoteSymbolEvent;
import cn.edu.wuse.musicxml.symbol.OutLineChangeEvent;
import cn.edu.wuse.musicxml.symbol.ScaleChangeEvent;
import cn.edu.wuse.musicxml.symbol.StaffDetailEvent;
import cn.edu.wuse.musicxml.symbol.StaffStructEvent;
import cn.edu.wuse.musicxml.symbol.TimeSymbolEvent;

/**
 * 打印时需要两部分重要的数据，一部分是整体的布局，一部分是
 * 乐谱元素，而乐谱元素的显示位置依赖于整体的布局，在抽象的
 * 打印监听器中给出默认的打印布局信息，在共同基类中只能使用
 * 一份布局数据
 * also a role of adpter
 *
 */
public class AbstractPrintListener implements PrintListener {

    protected static Layout layout = new Layout(8.9956f, 40f);

    public void doEndMeasure(EndMeasureEvent me) {
    }

    public void doEndPage(EndPageEvent pe) {
    }

    public void doEndSystem(EndSystemEvent se) {
    }

    public void doScoreStructChange(StaffStructEvent se) {
    }

    public void doScalingChange(ScaleChangeEvent sce) {
    }

    public void doLineWidthChange(OutLineChangeEvent ole) {
    }

    public void doLyricFontChange(FontChangeEvent fce) {
    }

    public void doMarginChange(OutLineChangeEvent ole) {
    }

    public void doMusicFontChange(FontChangeEvent fce) {
    }

    public void doPrintChange(OutLineChangeEvent ole) {
    }

    public void doWordFontChange(FontChangeEvent fce) {
    }

    public void doNoteSizeChange(OutLineChangeEvent ole) {
    }

    public void addMeasureSymbolEvent(MeasureSymbolEvent mse) {
    }

    public void doStaffDetailChange(StaffDetailEvent sfd) {
    }

    public void addClefSymbolEvent(ClefSymbolEvent cse) {
    }

    public void addKeySymbolEvent(KeySymbolEvent kse) {
    }

    public void doMeasureNumberingChange(MeasureNumberingChangeEvent mne) {
    }

    public void addTimeSymbolEvent(TimeSymbolEvent tse) {
    }

    public void addNoteSymbol(NoteSymbolEvent nse) {
    }

    public void addBeamPointEvent(BeamPointEvent bpe) {
    }
}
