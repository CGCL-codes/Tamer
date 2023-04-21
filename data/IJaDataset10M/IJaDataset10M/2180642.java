package org.springframework.samples.countries.web.translate;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
import pikes.ecma.Addition;
import pikes.ecma.Assignment;
import pikes.ecma.ExpressionStatement;
import pikes.ecma.FunctionCall;
import pikes.ecma.Identifier;
import pikes.ecma.MemberPropertyAccess;
import pikes.ecma.NewExpression;
import pikes.ecma.NullLiteral;
import pikes.ecma.ReturnStatement;
import pikes.ecma.StringLiteral;
import pikes.ecma.VariableStatement;

public class MethodBlockTranslatorTestCase {

    @Test
    public void empty() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("empty", 0, result);
        translator.visitCode();
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
        Assert.assertNull(result.body);
    }

    @Test
    public void emptyButNoReturn() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("empty", 0, result, true);
        translator.visitCode();
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
        Assert.assertEquals(new ReturnStatement(), result.body);
    }

    @Test
    public void emptyWithParameter() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("empty", 1, result);
        translator.visitCode();
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitLocalVariable("boo", null, null, null, null, 1);
        translator.visitEnd();
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(1, result.parameters.size());
        Assert.assertEquals("boo", result.parameters.get(0));
        Assert.assertNull(result.body);
    }

    @Test
    public void setFieldNull() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("setFieldNull", 0, result);
        translator.visitCode();
        translator.visitVarInsn(Opcodes.ALOAD, 0);
        translator.visitInsn(Opcodes.ACONST_NULL);
        translator.visitFieldInsn(Opcodes.PUTFIELD, null, "window", null);
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
        Assignment assignment = new Assignment(new MemberPropertyAccess("this", "window"), new NullLiteral());
        Assert.assertEquals(new ExpressionStatement(assignment), result.body);
    }

    @Test
    public void setParameterNull() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("setLocalNull", 1, result);
        translator.visitCode();
        translator.visitInsn(Opcodes.ACONST_NULL);
        translator.visitVarInsn(Opcodes.ASTORE, 1);
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitLocalVariable("local", null, null, null, null, 1);
        translator.visitEnd();
        Assert.assertEquals(new ExpressionStatement(new Assignment(new Identifier("local"), new NullLiteral())), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(1, result.parameters.size());
        Assert.assertEquals("local", result.parameters.get(0));
    }

    @Test
    public void setLocalNull() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("setLocalNull", 0, result);
        translator.visitCode();
        translator.visitInsn(Opcodes.ACONST_NULL);
        translator.visitVarInsn(Opcodes.ASTORE, 1);
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitLocalVariable("local", null, null, null, null, 1);
        translator.visitEnd();
        Assert.assertEquals(new VariableStatement("local", new NullLiteral()), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
    }

    @Test
    public void setFieldsFieldNull() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("setFieldsFieldNull", 0, result);
        translator.visitCode();
        translator.visitVarInsn(Opcodes.ALOAD, 0);
        translator.visitFieldInsn(Opcodes.GETFIELD, null, "window", null);
        translator.visitInsn(Opcodes.ACONST_NULL);
        translator.visitFieldInsn(Opcodes.PUTFIELD, null, "location", null);
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertEquals(new ExpressionStatement(new Assignment(new MemberPropertyAccess(new MemberPropertyAccess("this", "window"), "location"), new NullLiteral())), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
    }

    @Test
    public void returnNull() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("returnNull", 0, result);
        translator.visitCode();
        translator.visitInsn(Opcodes.ACONST_NULL);
        translator.visitInsn(Opcodes.ARETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertEquals(new ReturnStatement(new NullLiteral()), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
    }

    @Test
    public void returnNewObject() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("returnNull", 0, result);
        translator.visitCode();
        translator.visitTypeInsn(Opcodes.NEW, "java/lang/Object");
        translator.visitInsn(Opcodes.DUP);
        translator.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        translator.visitInsn(Opcodes.ARETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertEquals(new ReturnStatement(new NewExpression(new Identifier("Object"))), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
    }

    @Test
    public void returnNewString() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("returnNull", 0, result);
        translator.visitCode();
        translator.visitTypeInsn(Opcodes.NEW, "java/lang/String");
        translator.visitInsn(Opcodes.DUP);
        translator.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/String", "<init>", "()V");
        translator.visitInsn(Opcodes.ARETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertEquals(new ReturnStatement(new NewExpression(new Identifier("String"))), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
    }

    @Test
    public void returnNewStringWithParam() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("returnNull", 0, result);
        translator.visitCode();
        translator.visitTypeInsn(Opcodes.NEW, "java/lang/String");
        translator.visitInsn(Opcodes.DUP);
        translator.visitLdcInsn("boo");
        translator.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/String", "<init>", "(Ljava/lang/String;)V");
        translator.visitInsn(Opcodes.ARETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertEquals(new ReturnStatement(new NewExpression(new FunctionCall("String", new StringLiteral("boo")))), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
    }

    @Test
    @Ignore
    public void addDoubles() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("addDoubles", 1, result);
        translator.visitCode();
        translator.visitLdcInsn(new Double("14.5"));
        translator.visitVarInsn(Opcodes.DLOAD, 1);
        translator.visitInsn(Opcodes.DADD);
        translator.visitVarInsn(Opcodes.DSTORE, 3);
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitLocalVariable("n", null, null, null, null, 1);
        translator.visitLocalVariable("res", null, null, null, null, 3);
        translator.visitEnd();
        Assert.assertEquals(new VariableStatement("res", new Addition(new Identifier("14.5"), new Identifier("n"))), result.body);
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(1, result.parameters.size());
        Assert.assertEquals("n", result.parameters.get(0));
    }

    @Test
    public void HistoryGo() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("back", 0, result);
        translator.visitCode();
        translator.visitVarInsn(Opcodes.ALOAD, 0);
        translator.visitFieldInsn(Opcodes.GETFIELD, null, "history", null);
        translator.visitInsn(Opcodes.ICONST_M1);
        translator.visitMethodInsn(Opcodes.INVOKEVIRTUAL, null, "go", null);
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitEnd();
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(0, result.parameters.size());
        FunctionCall call = new FunctionCall(new MemberPropertyAccess(new MemberPropertyAccess("this", "history"), "go"));
        call.addArgument(new Identifier("-1"));
        Assert.assertEquals(new ExpressionStatement(call), result.body);
    }

    @Test
    public void SetWindowLocation() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("go", 1, result);
        translator.visitCode();
        translator.visitVarInsn(Opcodes.ALOAD, 0);
        translator.visitFieldInsn(Opcodes.GETFIELD, null, "window", null);
        translator.visitVarInsn(Opcodes.ALOAD, 1);
        translator.visitFieldInsn(Opcodes.PUTFIELD, null, "location", null);
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", null, null, null, null, 0);
        translator.visitLocalVariable("country", null, null, null, null, 1);
        translator.visitEnd();
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(1, result.parameters.size());
        Assert.assertEquals("country", result.parameters.get(0));
        Assignment assignment = new Assignment(new MemberPropertyAccess(new MemberPropertyAccess("this", "window"), "location"), new Identifier("country"));
        Assert.assertEquals(new ExpressionStatement(assignment), result.body);
    }

    @Test
    @Ignore
    public void SetWindowLocation1() throws Exception {
        MockTranslationResult result = new MockTranslationResult();
        MethodBlockTranslator translator = new MethodBlockTranslator("go", 1, result);
        translator.visitCode();
        translator.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        translator.visitInsn(Opcodes.DUP);
        translator.visitLdcInsn("/countries/main/detail.htm?code=");
        translator.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        translator.visitVarInsn(Opcodes.ALOAD, 1);
        translator.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        translator.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        translator.visitVarInsn(Opcodes.ASTORE, 2);
        translator.visitVarInsn(Opcodes.ALOAD, 0);
        translator.visitFieldInsn(Opcodes.GETFIELD, "org/springframework/samples/countries/web/MainPage", "window", "Lorg/springframework/samples/countries/web/bom/Window;");
        translator.visitVarInsn(Opcodes.ALOAD, 2);
        translator.visitFieldInsn(Opcodes.PUTFIELD, "org/springframework/samples/countries/web/bom/Window", "location", "Ljava/lang/String;");
        translator.visitInsn(Opcodes.RETURN);
        translator.visitLocalVariable("this", "Lorg/springframework/samples/countries/web/MainPage;", null, null, null, 0);
        translator.visitLocalVariable("country", "Ljava/lang/String;", null, null, null, 1);
        translator.visitLocalVariable("url", "Ljava/lang/String;", null, null, null, 2);
        translator.visitEnd();
        Assert.assertNotNull(result.parameters);
        Assert.assertEquals(1, result.parameters.size());
        Assert.assertEquals("country", result.parameters.get(0));
        Assignment assignment = new Assignment(new MemberPropertyAccess(new MemberPropertyAccess("this", "window"), "location"), new Identifier("country"));
        Assert.assertEquals(new ExpressionStatement(assignment), result.body);
    }
}
