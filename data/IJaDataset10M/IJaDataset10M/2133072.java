/**
 * Category.java 1.0 2007-4-14 下午11:28:01,创建 by yangfan
 */


package org.in4j.bbs.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 版块
 * @author 杨帆
 * @version 1.0 2007-4-14 下午11:28:01,创建
 */
public class Board
{
	private static final Timestamp DEFAULT_TIME = new Timestamp(0);
	private Integer id;
	private String name;
	private String description;
	private int topicCount;
	private int postCount;
	private String updateUser;
	private Timestamp updateTime = DEFAULT_TIME;
	private List<String> admins;
	private Category category;

	private Set<Topic> globalPinedTopics = new HashSet<Topic>();
	private Set<Topic> pinedTopics = new HashSet<Topic>();
	private Set<Topic> topics = new HashSet<Topic>();

	public Set<Topic> getPinedTopics()
	{
		return pinedTopics;
	}
	public void setPinedTopics(Set<Topic> pinedTopics)
	{
		this.pinedTopics = pinedTopics;
	}
	public void addPinedTopic(Topic pinedTopic)
	{
		pinedTopic.setBoard(this);
		this.pinedTopics.add(pinedTopic);
	}
	public Set<Topic> getTopics()
	{
		return topics;
	}
	public void setTopics(Set<Topic> topics)
	{
		this.topics = topics;
	}
	public void addTopic(Topic topic)
	{
		topic.setBoard(this);
		this.topics.add(topic);
	}
	public List<String> getAdmins()
	{
		return admins == null? new ArrayList<String>() : admins;
	}
	public void setAdmins(List<String> admins)
	{
		this.admins = admins;
	}

	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String desc)
	{
		this.description = desc;
	}
	public Category getCategory()
	{
		return category;
	}
	public void setCategory(Category category)
	{
		this.category = category;
	}
	public int getPostCount()
	{
		return postCount;
	}
	public void setPostCount(int posts)
	{
		this.postCount = posts;
	}
	public int getTopicCount()
	{
		return topicCount;
	}
	public void setTopicCount(int topics)
	{
		this.topicCount = topics;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getUpdateUser()
	{
		return updateUser;
	}
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("id", id).append("name", name).toString();
	}
	public Set<Topic> getGlobalPinedTopics()
	{
		return globalPinedTopics;
	}
	public void setGlobalPinedTopics(Set<Topic> globalPinedTopics)
	{
		this.globalPinedTopics = globalPinedTopics;
	}
	public void addTopicCount(int num)
	{
		this.setTopicCount(getTopicCount() + num);
	}
	public void addPostCount(int num)
	{
		this.setPostCount(getPostCount() + num);
	}

}
