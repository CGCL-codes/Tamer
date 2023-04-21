package com.javaPattern.Responsibility;

public class TestCoR {
	public static void main(String[] args) {
		// 这个handler的写法很少见啊！
		// 这就是生成的一条职责链
		// 个人认为关键在这里
		Handler handler = new HandlerTeacher(new HandlerStudent(
				new HandlerSchoolmaster(null)));

		Request studentReq = new RequestStudent();
		Request teacherReq = new RequestTeacher();
		Request schoolmasterReq = new RequestSchoolmaster();
		Request presidentReq = new RequestPresident();

		// 传入student request
		handler.handleRequest(studentReq);
		System.out.println("-----------");
		// 传入teacher request
		handler.handleRequest(teacherReq);
		System.out.println("-----------");
		// 传入schoolmaster request
		handler.handleRequest(schoolmasterReq);
		System.out.println("-----------");
		// 传入president request
		handler.handleRequest(presidentReq);
		System.out.println("-----------");
	}
}
