package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.VisitControlToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import antlr.collections.AST;

/**
 * antlr��AST�p�̃r�W�^�[�����ɂǂ̃m�[�h�ɐi�ނׂ�����U������ {@link AstVisitStrategy}�̃f�t�H���g����.
 * 
 * @author kou-tngt
 *
 */
public class AntlrAstVisitStrategy implements AstVisitStrategy<AST> {

    /**
     * �N���X�⃁�\�b�h��`��\���m�[�h�̓�����K�₳���邩�ǂ������w�肷��R���X�g���N�^.
     * @param intoClass �N���X��`�̓�����K�₳����ꍇ��true
     * @param intoMethod�@���\�b�h��`�̓�����K�₳����ꍇ��true
     */
    public AntlrAstVisitStrategy(final boolean intoClass, final boolean intoMethod) {
        this.intoClass = intoClass;
        this.intoMethod = intoMethod;
    }

    /**
     * ���token���\����ނ̃m�[�h�ɂ��āC���̎q�m�[�h��K�₷��K�v�����邩�ǂ����𔻒肷��.
     * ���̃��\�b�h���I�[�o�[���C�h���邱�Ƃɂ���āC{@link #guide(AstVisitor, AST, AstToken)}���\�b�h��
     * �r�W�^�[��U������ۂɁC�m�[�h�̓����ɗU�����邩�ǂ����𐧌䂷�邱�Ƃ��ł���.
     * 
     * @param token �q�m�[�h��K�₷��K�v�����邩�ǂ����𔻒肷��m�[�h�̃g�[�N��
     * @return �K�₷��K�v������ꍇ��true
     */
    public boolean needToVisitChildren(final AST node, final AstToken token) {
        if (token.isClassDefinition()) {
            return this.intoClass;
        } else if (token.isMethodDefinition()) {
            return this.intoMethod;
        } else if (token.equals(VisitControlToken.SKIP)) {
            return false;
        }
        return true;
    }

    /**
     * �N���X�����֗U�����邩�ǂ�����\���t�B�[���h
     */
    private final boolean intoClass;

    /**
     * ���\�b�h�����֗U�����邩�ǂ�����\���t�B�[���h
     */
    private final boolean intoMethod;
}
