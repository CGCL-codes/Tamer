package com.jiajun.ui;

import java.awt.event.ActionEvent;

public class CreateReportAction extends MyAction {

    public CreateReportAction(MainFrame frame) {
        super("��ɱ���", frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.openReoprtPanel();
    }
}
