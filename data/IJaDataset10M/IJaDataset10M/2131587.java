package base.cycle;

import base.CObject;
import base.CProject;
import base.module.CModule;
import base.value.CIntValue;
import java.util.LinkedList;
import parser.CParser;

public class CFor extends CModule {

    private LinkedList<CModule> Modules = new LinkedList<CModule>();

    private CObject Iterator;

    private CObject FromObject;

    private CObject ToObject;

    private enum ESection {

        Iterator, StartingValue, EndingValue
    }

    ;

    @Override
    public CObject Parse(LinkedList<String> Tokens) {
        SetFilePosition();
        ESection Section = ESection.Iterator;
        LinkedList<String> ForTokens = new LinkedList<String>();
        LinkedList<String> FromTokens = new LinkedList<String>();
        LinkedList<String> ToTokens = new LinkedList<String>();
        for (String Token : Tokens) {
            if (Token.equals("циклдля")) continue;
            if (Token.equals("от")) {
                Section = ESection.StartingValue;
            } else if (Token.equals("до")) {
                Section = ESection.EndingValue;
            } else {
                switch(Section) {
                    case Iterator:
                        ForTokens.add(Token);
                        break;
                    case StartingValue:
                        FromTokens.add(Token);
                        break;
                    case EndingValue:
                        ToTokens.add(Token);
                        break;
                }
            }
        }
        if (Section != ESection.EndingValue) CParser.Error();
        Iterator = CParser.ParseFunction(ForTokens);
        FromObject = CParser.ParseFunction(FromTokens);
        ToObject = CParser.ParseFunction(ToTokens);
        ParseModules(Modules, "цикла", "КонецЦикла");
        return this;
    }

    @Override
    public void Execute() {
        int To = ToObject.ToInt();
        CIntValue Value = new CIntValue(0);
        for (Value.Value = FromObject.ToInt(); Value.Value <= To; Value.Value++) {
            Iterator.EquateTo(Value);
            ExecuteModulesList(Modules);
            if (CProject.ExitStatus == CProject.EExitStatus.Break) {
                CProject.ExitStatus = CProject.EExitStatus.No;
                break;
            } else if (CProject.ExitStatus == CProject.EExitStatus.ExitFunction) {
                break;
            }
            CProject.ExitStatus = CProject.EExitStatus.No;
        }
    }
}
