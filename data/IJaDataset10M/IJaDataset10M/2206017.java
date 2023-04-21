package org.jp.frm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jp.bus.dao.CodeMasterDao;
import org.jp.bus.dao.impl.CodeMasterDaoImpl;
import org.jp.bus.dto.CodeValueNameDto;

public class CodeMaster {

    private static HashMap<String, List<CodeValueNameDto>> cache = new HashMap<String, List<CodeValueNameDto>>();

    private static final CodeMaster codeMaster = new CodeMaster();

    CodeMasterDao dao = new CodeMasterDaoImpl();

    private CodeMaster() {
        List<CodeValueNameDto> list = dao.getCodeName();
        if (list != null) {
            for (CodeValueNameDto dto : list) {
                cache.put(dto.getCodeGroup(), dao.getCodeNameByCodeGroup(dto));
            }
        }
    }

    public static CodeMaster instance() {
        return codeMaster;
    }

    /**
	 * ָ������codeGroup�Υ��`����󼯺Ϥ�ȡ��
	 * @param codeGroup
	 * @return
	 */
    public List<CodeValueNameDto> getCodeName(final String codeGroup) {
        if (cache.containsKey(codeGroup)) {
            return cache.get(codeGroup);
        }
        CodeValueNameDto dto = new CodeValueNameDto();
        dto.setCodeGroup(codeGroup);
        List<CodeValueNameDto> list = dao.getCodeNameByCodeGroup(dto);
        if (list != null && list.size() > 0) {
            cache.put(codeGroup, list);
        } else {
            list = new ArrayList<CodeValueNameDto>();
        }
        return list;
    }

    /**
	 * ָ������codeGroup��codeKey�Υ��`������ȡ��
	 * @param id
	 * @param key
	 * @return
	 */
    public CodeValueNameDto getCodeNameByGroupAndKey(final String codeGroup, final String codeKey) {
        List<CodeValueNameDto> list = getCodeName(codeGroup);
        CodeValueNameDto codeValueNameDto = null;
        if (list != null && list.size() > 0) {
            for (CodeValueNameDto dto : list) {
                if (dto.getCodeKey().equals(codeKey)) {
                    codeValueNameDto = dto;
                }
            }
        }
        return codeValueNameDto;
    }

    /**
	 * ���1��ȡ��
	 * @param codeGroup
	 * @param codeKey
	 * @return
	 */
    public String getCodeNy1(final String codeGroup, final String codeKey) {
        CodeValueNameDto dto = getCodeNameByGroupAndKey(codeGroup, codeKey);
        if (dto != null) {
            return dto.getCodeNy1();
        } else {
            return null;
        }
    }

    /**
	 * ���2��ȡ��
	 * @param codeGroup
	 * @param codeKey
	 * @return
	 */
    public String getCodeNy2(final String codeGroup, final String codeKey) {
        CodeValueNameDto dto = getCodeNameByGroupAndKey(codeGroup, codeKey);
        if (dto != null) {
            return dto.getCodeNy2();
        } else {
            return null;
        }
    }
}
