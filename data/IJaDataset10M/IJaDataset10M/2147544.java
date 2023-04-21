package Main.GUI;

import Parameters.ControlParameters;
import Parameters.LabelName;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import tools.DesignPatters.ColleagueJMenuItem;
import tools.DesignPatters.Mediator;

/**
 *
 * @author 郝国生  HAO Guo-Sheng, HAO Guo Sheng, HAO GuoSheng
 */
public class MyGAMenuBar extends JMenuBar {

    ColleagueJMenuItem f0 = new ColleagueJMenuItem(LabelName.ProblemName[0], 's', ControlParameters.F0), f1 = new ColleagueJMenuItem(LabelName.ProblemName[1], '1', ControlParameters.F1), f2 = new ColleagueJMenuItem(LabelName.ProblemName[2], '2', ControlParameters.F2), f3 = new ColleagueJMenuItem(LabelName.ProblemName[3], '3', ControlParameters.F3), f4 = new ColleagueJMenuItem(LabelName.ProblemName[4], '4', ControlParameters.F4), f5 = new ColleagueJMenuItem(LabelName.ProblemName[5], '5', ControlParameters.F5), f6 = new ColleagueJMenuItem(LabelName.ProblemName[6], '6', ControlParameters.F6), f7 = new ColleagueJMenuItem(LabelName.ProblemName[7], '7', ControlParameters.F7), f8 = new ColleagueJMenuItem(LabelName.ProblemName[8], '8', ControlParameters.F8), f9 = new ColleagueJMenuItem(LabelName.ProblemName[9], '9', ControlParameters.F9), f10 = new ColleagueJMenuItem(LabelName.ProblemName[10], '0', ControlParameters.F10), face = new ColleagueJMenuItem(LabelName.ProblemName[11], 'F', ControlParameters.Face), fashion = new ColleagueJMenuItem(LabelName.ProblemName[12], 'D', ControlParameters.Fashion), fractual = new ColleagueJMenuItem(LabelName.ProblemName[13], 'R', ControlParameters.Julia), maxColor = new ColleagueJMenuItem(LabelName.ProblemName[14], 'M', ControlParameters.OneMaxColor), maxHSV = new ColleagueJMenuItem(LabelName.ProblemName[15], 'H', ControlParameters.HSVOneMax), languageEnlish = new ColleagueJMenuItem(LabelName.languageName[0], 'N', ControlParameters.englishLanguage), languageChinese = new ColleagueJMenuItem(LabelName.languageName[1], 'C', ControlParameters.chineseLanguage), exitmenu = new ColleagueJMenuItem(LabelName.exit, 'x', ControlParameters.exitmenu), ranmidimenu = new ColleagueJMenuItem(LabelName.ProblemName[16], 'B', ControlParameters.RandMidiMusic), scoremenu = new ColleagueJMenuItem(LabelName.ProblemName[17], 'C', ControlParameters.ScoreMusic), chordmenu = new ColleagueJMenuItem(LabelName.ProblemName[18], 'D', ControlParameters.ChordMusic), commenu = new ColleagueJMenuItem("谱曲");

    public MyGAMenuBar(Mediator med) {
        f0.setMediator(med);
        f1.setMediator(med);
        f2.setMediator(med);
        f3.setMediator(med);
        f4.setMediator(med);
        f5.setMediator(med);
        f6.setMediator(med);
        f7.setMediator(med);
        f8.setMediator(med);
        f9.setMediator(med);
        f10.setMediator(med);
        face.setMediator(med);
        fashion.setMediator(med);
        fractual.setMediator(med);
        maxColor.setMediator(med);
        maxHSV.setMediator(med);
        languageEnlish.setMediator(med);
        languageChinese.setMediator(med);
        exitmenu.setMediator(med);
        ranmidimenu.setMediator(med);
        scoremenu.setMediator(med);
        chordmenu.setMediator(med);
        initMenues();
    }

    private void initMenues() {
        {
            JMenu file = new JMenu(LabelName.file);
            JMenu tools = new JMenu(LabelName.tools);
            JMenu run = new JMenu(LabelName.run);
            JMenu help = new JMenu(LabelName.help);
            {
                file.setMnemonic('F');
                tools.setMnemonic('T');
                run.setMnemonic('R');
                help.setMnemonic('H');
            }
            {
                JMenu tga = new JMenu(LabelName.tga);
                tga.setMnemonic('T');
                tga.add(f0);
                tga.addSeparator();
                tga.add(f1);
                tga.add(f2);
                tga.add(f3);
                tga.add(f4);
                tga.add(f5);
                tga.addSeparator();
                tga.add(f6);
                tga.add(f7);
                tga.addSeparator();
                tga.add(f8);
                tga.addSeparator();
                tga.add(f9);
                tga.add(f10);
                tga.addSeparator();
                file.add(tga);
            }
            {
                JMenu iga = new JMenu(LabelName.iga);
                JMenu igaAidMusic = new JMenu(LabelName.igaAidMusic);
                igaAidMusic.add(commenu);
                igaAidMusic.add(ranmidimenu);
                igaAidMusic.add(scoremenu);
                igaAidMusic.add(chordmenu);
                iga.setMnemonic('I');
                igaAidMusic.setMnemonic('C');
                iga.add(face);
                iga.addSeparator();
                iga.add(fashion);
                iga.addSeparator();
                iga.add(fractual);
                iga.addSeparator();
                iga.add(maxColor);
                iga.add(maxHSV);
                iga.addSeparator();
                iga.add(igaAidMusic);
                file.add(iga);
            }
            {
                file.addSeparator();
                file.add(exitmenu);
            }
            {
                JMenu language = new JMenu(LabelName.language);
                language.add(languageEnlish);
                language.add(languageChinese);
                tools.add(language);
            }
            {
                this.add(file);
                this.add(tools);
                this.add(run);
                this.add(help);
            }
        }
    }
}
