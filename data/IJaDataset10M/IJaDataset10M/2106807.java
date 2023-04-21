package code.dao.Hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import code.dao.ProgramDAO;
import code.model.Comment;
import code.model.Program;

public class ProgramDAOHibernate extends HibernateDaoSupport 
								implements ProgramDAO {
    //增加
	public long insertProgram(Program program){
		this.getHibernateTemplate().save(program);
		return program.getProgramid();
	}
	//删除
	@SuppressWarnings("unchecked")
	public long deleteProgram(long programid){
		Program program = getProgramByProgramid(programid);
		if(program==null)
			return 0;
		List<Comment>commentlist=(List<Comment>)this.getHibernateTemplate().find(
				"from Comment where programid = "+programid);
		this.getHibernateTemplate().deleteAll(commentlist);
		this.getHibernateTemplate().delete(program);
		return programid;
	}
	//查找id
	@SuppressWarnings("unchecked")
	public Program getProgramByProgramid(long eid){
		List<Program>programList = (List<Program>)this.getHibernateTemplate().find("from Program where programid = "+eid);
		if(programList.size() == 0)
			return null;
		return programList.get(0);
	}
	

	//查找名字
	@SuppressWarnings("unchecked")
	public List<Program> getProgramlistByName(String name){
		return (List<Program>)this.getHibernateTemplate().find(
				"from Program where name like \'%"+name+"%\' ");
	}
	@SuppressWarnings("unchecked")
	public List<Program> getProgramlistByField(String field) {
		return (List<Program>)this.getHibernateTemplate().find(
				"from Program where field like \'%"+field+"%\' ");
	}
	@SuppressWarnings("unchecked")
	public List<Program> getProgramlistByRegion(String region) {
		return (List<Program>)this.getHibernateTemplate().find(
				"from Program where region like \'%"+region+"%\' ");
	}
	@SuppressWarnings("unchecked")
	public List<Program> getProgramlistByType(String type) {
		return (List<Program>)this.getHibernateTemplate().find(
				"from Program where type like \'%"+type+"%\' ");
	}
	@SuppressWarnings("unchecked")
	public List<Program> getProgramlistByInfo
	(String region,String type,String field){
		return (List<Program>)this.getHibernateTemplate().find(
				"from Program where region like \'%"+region+"%\'" +
						"and type like \'%"+type+"%\'" +
								"and field like \'%"+field+"%\'"
				
		
		);	
	};;
	
	
	@SuppressWarnings("unchecked")
	public List<Program> getProgramlistByUserid(long userid,int pageth){
		return (List<Program>)this.getHibernateTemplate().find(
				"from Program where userid = "+userid);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public double getGradeByProgramid(long programid){
		List<Double> avglist = (List<Double>)this.getHibernateTemplate().find(
				"select avg(grade) from Comment where programid = "+programid);
		if(avglist.get(0)==null)
			return 3;
		return avglist.get(0);
	}

	public void modifyProgram(Program program) {
		this.getHibernateTemplate().update(program);
	}
}
