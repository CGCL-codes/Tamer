package cn.vlabs.clb.search.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import cn.vlabs.clb.CLBException;
import cn.vlabs.clb.GeneralDataBaseException;
import cn.vlabs.clb.search.SearchResult;
import cn.vlabs.clb.security.permission.Resource;

public interface QueryDAO {

    /**
	 * ����ݿ��в�ѯ�����µ��ĵ�
	 * 
	 * @param who
	 *            ��ѯ˭���µģ�����ǿ����ѯ�����µġ�
	 * @return ����������������
	 */
    Collection<SearchResult> getRecentUpdates(String who, Date before) throws CLBException;

    /**
	 * ��ѯָ����Ա�������ĵ�
	 * 
	 * @param who
	 *            ����ѯ����Ա���û�����Ϊ��
	 * @param begin
	 * 			  ��ʼ��ʱ�䣬���ʱ��Ϊ�գ�����ʾ���е��ĵ���
	 * @return ���ط���������������
	 */
    Collection<SearchResult> getMyDocuments(String who, Date begin) throws CLBException;

    /**
	 * ��ѯ��ָ����ǩ���ĵ�
	 * 
	 * @param tags
	 *            ��Ҫ��ı�ǩ
	 * @return ����������������
	 * @throws CLBException
	 */
    Collection<SearchResult> getDocsWith(String[] tags) throws CLBException;

    /**
	 * ͳ��Ŀǰϵͳ�еı�ǩʹ��״��
	 * @return ���ĵ�Ϊ��֯��λ�ı�ǩ��Ϣ��
	 * @throws CLBException
	 */
    Collection<Resource> getTagsInfo() throws CLBException;

    /**
	 * ��ѯ�ҵ��ĵ���������Ϣ
	 * @param who ����ѯ���û�
	 * @return ����������ĵ���ID�б�
	 * @throws CLBException
	 */
    Collection<Resource> getMyDocCount(String who) throws CLBException;

    /**
	 * ��ѯ�����µ��ĵ�������
	 * @param before �����µĲ�ѯʱ���
	 * @return
	 * @throws CLBException
	 */
    Collection<Resource> getRecentCount(Date before) throws CLBException;

    /**
	 * ��ѯĳ��ʱ����ڸ��µ��ĵ�
	 * @param begin ʱ��εĿ�ʼʱ��
	 * @param end ʱ��εĽ���ʱ��
	 * @return
	 * @throws CLBException
	 */
    Collection<SearchResult> getDocsBetween(Date begin, Date end) throws CLBException;

    /**
	 * ��ѯ��Щ�ĵ�����Ϣ
	 * @param docids
	 * @return
	 * @throws CLBException
	 */
    Collection<SearchResult> getDocs(ArrayList<Integer> docids) throws CLBException;

    /**
	 * ģ��ƥ��
	 * @param input
	 * @return
	 * @throws GeneralDataBaseException
	 */
    Collection<SearchResult> searchAllField(String input) throws GeneralDataBaseException;
}
