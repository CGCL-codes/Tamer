package com.memoire.vainstall.tui;

import java.io.*;
import com.memoire.vainstall.*;

public class TuiReadmeStep extends TuiDefaultStep implements VAReadmeStep {

    private boolean accepted_ = false;

    public TuiReadmeStep() {
        super();
    }

    public void setText(InputStream lic) {
        TuiWizard.clear();
        TuiWizard.title();
        TuiWizard.println(VAGlobals.i18n("TuiReadmeStep_Readme"));
        TuiWizard.separator();
        TuiWizard.println("");
        if (lic == null) {
            TuiWizard.println(VAGlobals.i18n("TuiReadmeStep_NoReadme"));
        } else {
            try {
                if (!TuiWizard.skip) {
                    LineNumberReader in = new LineNumberReader(new InputStreamReader(lic, "UTF-8"));
                    String line = in.readLine();
                    int n = 3;
                    while (line != null) {
                        line = line.replace('\014', ' ');
                        TuiWizard.println(line);
                        n++;
                        if (n % 21 == 0) {
                            TuiWizard.enter();
                            TuiWizard.clear();
                            TuiWizard.title();
                            TuiWizard.println(VAGlobals.i18n("TuiReadmeStep_Readme"));
                            TuiWizard.separator();
                            TuiWizard.println("");
                            n = 3;
                        }
                        line = in.readLine();
                    }
                    in.close();
                    if (n % 21 != 0) {
                        while (n % 21 != 0) {
                            TuiWizard.println("");
                            n++;
                        }
                        TuiWizard.enter();
                    }
                }
                TuiWizard.clear();
                TuiWizard.title();
                TuiWizard.println(VAGlobals.i18n("TuiReadmeStep_Readme"));
                TuiWizard.separator();
                TuiWizard.println("");
                TuiWizard.println(VAGlobals.i18n("TuiReadmeStep_Thanks"));
                for (int i = 5; i < 22; i++) TuiWizard.println("");
                TuiWizard.info();
                TuiWizard.println("");
            } catch (IOException ex) {
            }
        }
    }
}
