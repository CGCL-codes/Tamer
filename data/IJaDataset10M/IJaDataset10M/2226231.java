package yaw.cjef.templates.bu.model.createTestdata;

import java.io.*;
import yaw.core.codegenerator.ICodeGenerator;

public class Testdata_java implements ICodeGenerator<Model> {

    public void generate(Writer writer, Model model) throws IOException {
        writer.write("/* \r\n * $Id: Testdata_java.java 49 2009-11-25 11:10:08Z tlh2000 $\r\n * \r\n * ");
        writer.write(model.getProjectName());
        writer.write("\r\n * ");
        writer.write(model.getCopyright());
        writer.write("\r\n * ");
        writer.write(model.getLegal());
        writer.write("\r\n */\r\npackage ");
        writer.write(model.getPackageNameTest(model.subPackageTargetName));
        writer.write(";\r\n\r\n//{{USER-IMPORT\r\nimport ");
        writer.write(model.getPackageNameTest(""));
        writer.write(".*;\r\nimport ");
        writer.write(model.getPackageNameGL());
        writer.write(".*;\r\nimport ");
        writer.write(model.getPackageNameDTO(model.subPackageName));
        writer.write(".*;\r\nimport ");
        writer.write(model.getPackageNameUsecase(""));
        writer.write(".*;\r\nimport ");
        writer.write(model.getPackageNameUsecase(model.subPackageTargetName));
        writer.write(".*;\r\nimport ");
        writer.write(model.getPackageNameModel(model.subPackageTargetName));
        writer.write(".*;\r\n\r\nimport de.carus.cjfc.tfc.bu.model.*;\r\nimport de.carus.cjfc.tfc.bu.usecase.*;\r\nimport de.carus.cjfc.tfc.util.*;\r\nimport de.carus.cjfc.tfc.val.*;\r\nimport de.carus.cjfc.tfc.test.*;\r\n\r\n//}}USER-IMPORT\r\n\r\n/**\r\n * Testdatengenerator für Modell ");
        writer.write(model.modelName);
        writer.write(".\r\n *\r\n * @author ");
        writer.write(model.getUser());
        writer.write("\r\n */\r\npublic class Create");
        writer.write(model.modelName);
        writer.write(" extends TMUseCaseCaller {\r\n  private static final TMLogger logger = new TMLogger(Create");
        writer.write(model.modelName);
        writer.write(".class);\r\n\r\n  static {\r\n    TMStaticInitializer.init(Create");
        writer.write(model.modelName);
        writer.write(".class);\r\n  }  \r\n  \r\n");
        writer.write(model.getMemberDeklaration());
        writer.write("\r\n  public Create");
        writer.write(model.modelName);
        writer.write("(");
        writer.write(model.getMemberParam());
        writer.write("){\r\n");
        writer.write(model.getMemberParamInit());
        writer.write("  }\r\n  \r\n  public Create");
        writer.write(model.modelName);
        writer.write("( ");
        writer.write(model.getModel());
        writer.write(" model) {\r\n    this.model = model;\r\n  }\r\n  \r\n  public void init");
        writer.write(model.modelName);
        writer.write("( TMTestContext ctx,TMUseCaseMgr app, ");
        writer.write(model.getModel());
        writer.write(" o ) throws TMValidateException {\r\n    super.init(ctx,app, o);\r\n\r\n");
        writer.write(model.getMemberInit());
        writer.write("  }\r\n  \r\n  public void init( TMTestContext ctx,TMUseCaseMgr app, TMModel o ) throws TMValidateException {\r\n    this.init");
        writer.write(model.modelName);
        writer.write("(ctx,app,( ");
        writer.write(model.getModel());
        writer.write(")o);\r\n  }\r\n  public  ");
        writer.write(model.getModel());
        writer.write(" get");
        writer.write(model.modelName);
        writer.write("( TMUseCaseMgr app ) throws TMValidateException {\r\n    return ( ");
        writer.write(model.getModel());
        writer.write(")getModel(app);\r\n  }\r\n  public  ");
        writer.write(model.getModel());
        writer.write(" get");
        writer.write(model.modelName);
        writer.write("( TMTestContext ctx,TMUseCaseMgr app ) throws TMValidateException {\r\n    return ( ");
        writer.write(model.getModel());
        writer.write(")getModel(ctx,app);\r\n  }\r\n  public TMUEditorMgr createEditorMgr(TMUseCaseMgr parent) {\r\n    return new ");
        writer.write(model.getEditor());
        writer.write(".Mgr(parent);\r\n  }\r\n}");
    }
}
