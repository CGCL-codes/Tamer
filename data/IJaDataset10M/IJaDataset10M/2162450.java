package net.sourceforge.picdev.components;

import net.sourceforge.picdev.util.HexUtils;
import java.awt.BorderLayout;

public class CpuDebugWindow extends DebugToolWindow {

    public CpuDebugWindow() {
        super.createWindow();
        createContent();
    }

    public void updateView() {
        if (getPic() == null) return;
        updateDebugWindow();
        content.repaint();
    }

    private void createContent() {
        adresses.setText("             BIN        HEX  DEC  CHAR  ");
        adresses2.setText("     W\n     Z\n     C\n0x00 INDF \n0x03 STATUS \n0x04 FSR \n0x05 PORTA \n0x06 PORTB \n0x08 EEDATA \n0x09 EEADR \n0x0B INTCON\n0x85 TRISA \n0x86 TRISB ");
        content.add(adresses, BorderLayout.NORTH);
        content.add(adresses2, BorderLayout.WEST);
        content.add(textarea, BorderLayout.CENTER);
    }

    /**
	 * this function updates the Cpu debug window
	 */
    public void updateDebugWindow() {
        PIC pic = getPic();
        if (pic == null) return;
        PICCPU cpu = pic.cpu;
        Ram ram = cpu.ram;
        StringBuilder fsr_map = new StringBuilder();
        fsr_map.append(" ").append(HexUtils.intToBinString(cpu.w)).append("   ").append(HexUtils.intToHexString(cpu.w)).append("   ").append(String.valueOf(cpu.w)).append("   ").append(String.valueOf((char) cpu.w)).append("\n");
        fsr_map.append(" ").append("0000000").append(String.valueOf((ram.fetch(PICCPU.STATUS) & PICCPU.Z) >> 2)).append("   ").append(HexUtils.intToHexString((ram.fetch(PICCPU.STATUS) & PICCPU.Z) >> 2)).append("   ").append(String.valueOf((ram.fetch(PICCPU.STATUS) & PICCPU.Z) >> 2)).append("\n");
        fsr_map.append(" ").append("0000000").append(String.valueOf((ram.fetch(PICCPU.STATUS) & PICCPU.C))).append("   ").append(HexUtils.intToHexString((ram.fetch(PICCPU.STATUS) & PICCPU.C))).append("   ").append(String.valueOf((ram.fetch(PICCPU.STATUS) & PICCPU.C))).append("\n");
        fsr_map.append(" ").append(HexUtils.intToBinString((short) ram.fetch(0x00))).append("   ").append(HexUtils.intToHexString((short) ram.fetch(0x00)));
        fsr_map.append("   ").append(String.valueOf((short) ram.fetch(0x00))).append("\n");
        for (int i = 3; i < 7; i++) fsr_map.append(" ").append(HexUtils.intToBinString((short) ram.fetch(i))).append("   ").append(HexUtils.intToHexString((short) ram.fetch(i))).append("   ").append(String.valueOf((short) ram.fetch(i))).append("   ").append(String.valueOf((char) ram.fetch(i))).append("\n");
        fsr_map.append(" ").append(HexUtils.intToBinString((short) ram.fetch(0x08))).append("   ").append(HexUtils.intToHexString((short) ram.fetch(0x08))).append("   ").append(String.valueOf((short) ram.fetch(0x08))).append("\n");
        fsr_map.append(" ").append(net.sourceforge.picdev.util.HexUtils.intToBinString((short) ram.fetch(0x09))).append("   ").append(HexUtils.intToHexString((short) ram.fetch(0x09))).append("   ").append(String.valueOf((short) ram.fetch(0x09))).append("\n");
        fsr_map.append(" ").append(HexUtils.intToBinString((short) ram.fetch(0x0B))).append("   ").append(HexUtils.intToHexString((short) ram.fetch(0x0B))).append("   ").append(String.valueOf((short) ram.fetch(0x0B))).append("\n");
        fsr_map.append(" ").append(HexUtils.intToBinString((short) ram.fetch(0x85))).append("   ").append(HexUtils.intToHexString((short) ram.fetch(0x85))).append("   ").append(String.valueOf((short) ram.fetch(0x85))).append("\n");
        fsr_map.append(" ").append(HexUtils.intToBinString((short) ram.fetch(0x86))).append("   ").append(HexUtils.intToHexString((short) ram.fetch(0x86))).append("   ").append(String.valueOf((short) ram.fetch(0x86)));
        display(fsr_map.toString());
    }
}
