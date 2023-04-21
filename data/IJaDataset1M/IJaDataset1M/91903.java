package cn.pfcg.rally.domain.service;

import cn.pfcg.rally.exception.ServiceException;

/**
 * @author figol
 */
public interface SelectQuestionService extends QuestionService {

    /**
	 * ¼��ѡ�����ѡ��
	 * 
	 * @param optionNumber
	 *            ѡ��ı�ţ�����A B C��
	 * @param optionContent
	 *            ѡ�������
	 */
    public void enterOption(String optionNumber, String optionContent);

    /**
	 * ¼����ȷ��
	 * 
	 * @param optionNumber
	 *            ��ȷѡ��
	 */
    public void enterAnswer(String optionNumber);

    /**
	 * ���������¼�룬���⽫��������
	 */
    public void endQuestion() throws ServiceException;
}
