package cn.jsprun.dao;

import java.util.List;
import cn.jsprun.domain.Membermagics;

public interface MemberMagicsDao {

    public List<Membermagics> getMemberMagics(int userid);

    public List<Membermagics> getMemberMagics(int userid, short magicid);
}
